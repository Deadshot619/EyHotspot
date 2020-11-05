package com.ey.hotspot.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.database.wifi_info.WifiInfoDatabase
import com.ey.hotspot.database.wifi_info.WifiInfoDatabaseDao
import com.ey.hotspot.database.wifi_info.WifiInformationTable
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.SpeedTestRequest
import com.ey.hotspot.network.request.ValidateWifiRequest
import com.ey.hotspot.network.request.WifiLoginRequest
import com.ey.hotspot.network.request.WifiLogoutRequest
import com.ey.hotspot.network.response.ValidateWifiResponse
import com.ey.hotspot.utils.CHANNEL_ID
import com.ey.hotspot.utils.SpeedTestUtils
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.SpeedTestModes
import com.ey.hotspot.utils.constants.checkWifiContainsKeywords
import com.ey.hotspot.utils.constants.getDeviceId
import com.ey.hotspot.utils.extention_functions.convertBpsToMbps
import com.ey.hotspot.utils.extention_functions.extractWifiName
import com.ey.hotspot.utils.extention_functions.getUserLocation
import com.ey.hotspot.utils.extention_functions.toServerFormat
import com.ey.hotspot.utils.getNotification
import com.ey.hotspot.utils.wifi_notification_key
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*


class WifiService : Service() {
    companion object {
        //Variable to indicate whether the service is running
        private var _isRunning = false
        val isRunning: Boolean
            get() = _isRunning

        var callingLoginApiFromSpeedTest = false
    }

    private lateinit var connecManagerWifi: ConnectivityManager        //For Wifi
    private lateinit var connecManagerAny: ConnectivityManager

    private lateinit var wifiManager: WifiManager
    private lateinit var database: WifiInfoDatabaseDao

    //Current WiFi service id
    private val WIFI_SERVICE_ID = 1

    //Holds value of currently inserted DB data
    private var _currentlyInsertedDataId = -1L
    private var _currentWifiId = 0
    private val DEVICE_ID = getDeviceId()


    //Variable to store whether the connected wifi is Our open wifi
    private var isItOurWifi = false
    private var validateWifiSuccessful =
        false  //True, if wifi is validated successfully, else false

    private var loginSuccessfulWithSpeedZero: Boolean = false
    private var requireApiCall: Boolean = true


    //Create a Coroutine Scope & job
    private val serviceJob = Job()
    private val coroutineScope = CoroutineScope(serviceJob + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        //indicate Service is running
        _isRunning = true

        //Get connectivity Manager
        connecManagerWifi = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connecManagerAny = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //Get Wifi Manager
        wifiManager = applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val networkRequestWiFi: NetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()


        //get instance of database
        database = WifiInfoDatabase.getInstance(application).wifiInfoDatabaseDao

        //Wifi
        connecManagerWifi.registerNetworkCallback(networkRequestWiFi, wifiNetworkCallback)

        //For any network with internet
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connecManagerAny.registerDefaultNetworkCallback(anyNetworkCallback)
        } else {
            val request =
                NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build()
            connecManagerAny.registerNetworkCallback(request, anyNetworkCallback)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val input = intent?.getStringExtra(wifi_notification_key) ?: ""

        if (input != "")
        //Start foreground service with notification
            startForeground(
                WIFI_SERVICE_ID,
                getNotification(
                    this,
                    getString(R.string.wifi_service_label),
//                    input,
                    getString(R.string.monitoring_wifi),
                    CHANNEL_ID
                )
            )

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        //indicate EService is not running
        _isRunning = false

        //When wifi is lost/disconnected, add wifi logout time
        coroutineScope.launch {
            //Add Logout Time
            updateLogoutTimeInDb()
        }

        coroutineScope.cancel()
    }


    //Network callback for WIFI
    private var wifiNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network?) {
            //When wifi is lost/disconnected, add wifi logout time
            coroutineScope.launch {
                //Add Logout Time
                updateLogoutTimeInDb()
            }
        }

        override fun onAvailable(network: Network?) {
            /* coroutineScope.launch {
                 //Get last inserted data & do things accordingly
                 getLastInsertedData()
             }*/
            /*
             *  Do not validate wifi if the user logs in or skips for first time
             */
            if (HotSpotApp.prefs?.getFirstTimeLoginOrSkipped() == true) return

            //Wifi Ssid
            var wifiSsid = wifiManager.connectionInfo.ssid.extractWifiName()/* + "-Turtlemint"*/
            if (wifiSsid.contains(Constants.UNKNOWN_SSID)) {
                Thread.sleep(1000)
                wifiSsid = wifiManager.connectionInfo.ssid.extractWifiName()/* + "-Turtlemint"*/
            }

            if (!wifiSsid.contains(Constants.UNKNOWN_SSID)) {    //If the wifi is "unknown ssid", then skip it
                /*
                 *  Check if the wifi name is present in wifi keywords
                 */
                isItOurWifi = checkWifiContainsKeywords(wifiSsid)
                //true   //TODO 27/07/2020: Remove True, when live
            } else {
                isItOurWifi = false
            }

            //If the wifi contains some special keywords i.e it belongs to current wifi then validate current wifi
            if (isItOurWifi) {
                applicationContext.getUserLocation { lat, lng ->
                    if (lat != null && lng != null) {   //If Location is available
                        coroutineScope.launch {
                            //Validate WiFi
                            validateWifi(wifiSsid = wifiSsid, lat = lat, lng = lng)
                        }
                    }   //TODO 28/07/2020: What if User Location is not available?
                    else {
                        //This wifi is not our wifi
                        ContextCompat.startForegroundService(
                            applicationContext,
                            Intent(applicationContext, WifiService::class.java).apply {
                                putExtra(
                                    wifi_notification_key,
                                    getString(R.string.wifi_not_validated_label)
                                )
                            })
                    }
                }

                //This will initially show the notification as wifi connected
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, WifiService::class.java).apply {
                        putExtra(
                            wifi_notification_key,
                            String.format(
                                getString(R.string.calculating_wifi_speed_label),
                                wifiManager.connectionInfo.ssid
                            )
                        )
                    })
            } else {   //This wifi is not our wifi
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, WifiService::class.java).apply {
                        putExtra(
                            wifi_notification_key,
                            getString(R.string.wifi_not_validated_label)
                        )
                    })
            }
        }
    }

    /**
     * This Network callback is for to run on any available network.
     * This first checks db for any available data where logout time is present, if it exists it then calls Logout Api
     */
    private var anyNetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            /*coroutineScope.launch {
                Timber.tag("NETWORK CALLBACK : ").d(network.toString())
                //Get last inserted data & do things accordingly
                getLastInsertedData()
            }*/
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
//            super.onCapabilitiesChanged(network, networkCapabilities)
            val connected =
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            if (connected)
                coroutineScope.launch {
                    Timber.tag("NETWORK CALLBACK : ").d(network.toString())
                    //Get last inserted data & do things accordingly
                    getLastInsertedData()
                }
        }

        override fun onLost(network: Network) {

        }
    }

    /*
     * Method to validate if the wifi is company's WiFi. If yes, then Calculate wifi download speed
     */
    private suspend fun validateWifi(wifiSsid: String, lat: Double, lng: Double) {
        val request = ValidateWifiRequest(wifiSsid, lat = lat, lng = lng)

        //Set this variable to false
        loginSuccessfulWithSpeedZero = false

        DataProvider.validateWifi(
            request = request,
            success = {
                if (it.status) {
                    validateWifiSuccessful = true
                    _currentWifiId = it.data.id

                    coroutineScope.launch {
                        //Call login api with 0.0 average speed & calculate speed

                        getLastInsertedDataForLogin(it.data, it.data._wifi_name)

                        /*WifiLogUtils.getLastInsertedDataForLogin(
                            wifiSsid = it.data._wifi_name,
                            database = database,
                            validateData = it.data,
                            callWifiLogin = { _, _, _, _ ->
                                callWifiLogin(wifiSsid = wifiSsid, wifiId = it.data.id, deviceId = DEVICE_ID, averageSpeed = 0.0)},
                            calculateSpeed = { _, _, _ ->
                                calculateSpeed(
                                    wifiSsid = wifiSsid,
                                    wifiId = it.data.id,
                                    deviceId = DEVICE_ID
                                )},
                            wifiLoginNotRequired = {}
                        )*/

                        /*callWifiLogin(wifiId = it.data.id, deviceId = DEVICE_ID, averageSpeed = 0.0)

                        calculateSpeed(
                            wifiId = it.data.id,
                            deviceId = DEVICE_ID
                        )*/
                    }
                } else {
                    validateWifiSuccessful = false
                    ContextCompat.startForegroundService(
                        applicationContext,
                        Intent(applicationContext, WifiService::class.java).apply {
                            putExtra(
                                wifi_notification_key,
                                getString(R.string.wifi_not_validated_label)
                            )
                        })
                }
            },
            error = {
                validateWifiSuccessful = false
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(applicationContext, WifiService::class.java).apply {
                        putExtra(
                            wifi_notification_key,
                            getString(R.string.wifi_not_validated_label)
                        )
                    })
            }
        )
    }

    //Method to call WifiLogin Api
    private fun callWifiLogin(
        wifiSsid: String,
        wifiId: Int,
        averageSpeed: Double,
        deviceId: String
    ) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                //First delete data from DB
                database.deleteAllDataFromDb()
            }

            val request = WifiLoginRequest(
                wifi_id = wifiId,
                average_speed = averageSpeed.toInt(),
                device_id = deviceId
            )

            DataProvider.wifiLogin(
                request = request,
                success = {
                    if (it.status) {

                        //Set this login status accordingly
                        loginSuccessfulWithSpeedZero = averageSpeed == 0.0

                        //When login is successful, Delete & Insert data into table
                        coroutineScope.launch {
                            deleteAndInsertData(
                                wifiSsid = wifiSsid,
                                wifiId = wifiId,
                                averageSpeed = averageSpeed
                            )
                        }

                    } else {
                        //Set login status to false as login wasn't successful
                        loginSuccessfulWithSpeedZero = false
                    }
                },
                error = {
                    //Set login status to false as login wasn't successful
                    loginSuccessfulWithSpeedZero = false
                }
            )
        }
    }

    /*
        This method will be called after Login is done & Speed Test has been completed/Failed .
     */
    private suspend fun callSetWifiSpeedTestApi(wifiId: Int, deviceId: String, speed: Double) {
        val request =
            SpeedTestRequest(
                wifi_id = wifiId,
                device_id = deviceId,
                average_speed = speed,
                mode = SpeedTestModes.BACKGROUND.value
            )

        DataProvider.wifiSpeedTest(
            request = request,
            success = {
            },
            error = {
            }
        )
    }

    /*
     *  Method to call WifiLogout Api.
     *  This api will be called when a wifi connection will be available
     */
    private suspend fun callWifiLogout(dbId: Long, wifiId: Int, deviceId: String, logoutAt: Date) {
        val request = WifiLogoutRequest(
            wifi_id = wifiId,
            device_id = deviceId,
            logout_at = logoutAt.toServerFormat()
        )

        DataProvider.wifiLogout(
            request = request,
            success = {
                if (it.status) {
                    //If logout is successful, then update sync status of the data in DB
                    coroutineScope.launch {
                        updateSyncStatusOfDataInDb(dbId = dbId, syncStatus = it.status)
                    }
                }
            },
            error = {
            }
        )
    }

    /*
     *  This method will delete all data from DB then insert a new row.
     *  This will be called when user's wifi data has been successfully logged in on Server.
     */
    private suspend fun deleteAndInsertData(wifiSsid: String, wifiId: Int, averageSpeed: Double) {
        withContext(Dispatchers.IO) {
            synchronized(this) {
                //First delete data from DB
                database.deleteAllDataFromDb()

                //Then insert new data  from DB
                _currentlyInsertedDataId = database.insert(
                    WifiInformationTable(
                        wifiSsid = wifiSsid,
                        connectedOn = Calendar.getInstance(),
                        downloadSpeed = averageSpeed.toString(),
                        wifiId = wifiId,
                        accessToken = HotSpotApp.prefs?.getAccessToken()
                    )
                )
            }
        }
    }

    /*
     *  This method will retrieve last inserted data from DB based on their ID as it increments linearly.
     *  Then If the data is present, not synced & has disconnected time, we will call logout Wifi api
     */
    private suspend fun getLastInsertedData() {
        withContext(Dispatchers.IO) {
            val data = database.getLastInsertedData()
            if (data.isNotEmpty() && !data[0].synced && data[0].disconnectedOn != null)
                callWifiLogout(
                    dbId = data[0].id,
                    wifiId = data[0].wifiId,
                    deviceId = DEVICE_ID,
                    logoutAt = data[0].disconnectedOn!!.time
                )
        }
    }

    private suspend fun getLastInsertedDataForLogin(
        validateData: ValidateWifiResponse,
        wifiSsid: String
    ) {
        withContext(Dispatchers.IO) {
            //Set this variable to true/
            requireApiCall = true

            //Get wifi login data from db
            val data = database.getLastInsertedData()

            //Check if data is present
            if (data.isNotEmpty()) {
                //Check whether user is logged in or not
                if (HotSpotApp.prefs!!.getAccessToken().isNotEmpty()) {  //Logged in user
                    //If user already has access token stored in db, that means the wifi is logged in for current user
                    //So if token is present && disconnect time is not present, set this to false, else true
                    if (HotSpotApp.prefs!!.getAccessToken() == data[0].accessToken && (data[0].disconnectedOn.toString() == "null" &&
                                data[0].wifiId == validateData.id && (Calendar.getInstance().timeInMillis - data[0].connectedOn.timeInMillis) <= Constants.WIFI_LOGOUT_TIME)
                    )
                        requireApiCall = false
                    else
                        requireApiCall = true
                } else {    //Skipped user
                    //If user already has access token stored in db as null, that means the wifi is logged in for current skipped user
                    //So if token is null, set this to false, else true
                    if (data[0].accessToken.isNullOrEmpty() && (data[0].disconnectedOn.toString() == "null" &&
                                data[0].wifiId == validateData.id && (Calendar.getInstance().timeInMillis - data[0].connectedOn.timeInMillis) <= Constants.WIFI_LOGOUT_TIME)
                    )
                        requireApiCall = false
                    else
                        requireApiCall = true
                }
            }

            //If wifi is already logged in, then don't call Wifi Login Api & set the flag to true
            if (requireApiCall && !callingLoginApiFromSpeedTest) {
                callWifiLogin(
                    wifiSsid = wifiSsid,
                    wifiId = validateData.id,
                    deviceId = DEVICE_ID,
                    averageSpeed = 0.0
                )

                //Calculate speed anyways
                calculateSpeed(
                    wifiSsid = wifiSsid,
                    wifiId = validateData.id,
                    deviceId = DEVICE_ID
                )
            } else
                loginSuccessfulWithSpeedZero = true


        }
    }

    /*
     *  Method to Update Logout Time in DB.
     *  Will be called when wifi is disconnected/lost
     */
    private suspend fun updateLogoutTimeInDb() {
        //Update data in table
        withContext(Dispatchers.IO) {
            val lastInsertedData = database.getLastInsertedData()

            if (lastInsertedData.isNotEmpty() && lastInsertedData[0].disconnectedOn.toString() == "null") {
                val success = database.updateWifiInfoData(
                    id = lastInsertedData[0].id,
                    disconnectedOn = Calendar.getInstance()
                )

                /*if (_currentlyInsertedDataId >= 0L) {   //Update data only if key is >= 0
                    val success = database.updateWifiInfoData(
                        id = _currentlyInsertedDataId,
                        disconnectedOn = Calendar.getInstance()
                    )
    */
                if (success == 1)       //If row updated successfully, then change current id
                    _currentlyInsertedDataId = -1
            }
        }
    }

    /*
     *  This method will update current sync status of Data in DB.
     *  Will be called when WiFi logout api has been called & wifi has successfully logged out
     */
    private suspend fun updateSyncStatusOfDataInDb(dbId: Long, syncStatus: Boolean) {
        withContext(Dispatchers.IO) {
            database.updateSyncStatus(id = dbId, sync = syncStatus)
        }
    }

    /*
     *  Method to calculate speed
     */
    private fun calculateSpeed(wifiSsid: String, wifiId: Int, deviceId: String) {
        coroutineScope.launch(Dispatchers.IO) {
            SpeedTestUtils.calculateSpeed(
                onCompletedReport = {       //When speed test is completed successfully
                    //get download speed
                    val downloadSpeed = it?.transferRateBit?.convertBpsToMbps()

                    //Start Service
                    ContextCompat.startForegroundService(
                        applicationContext,
                        Intent(applicationContext, WifiService::class.java).apply {
                            putExtra(
                                wifi_notification_key,
                                String.format(
                                    getString(R.string.calculated_wifi_speed_label),
                                    wifiManager.connectionInfo.ssid.extractWifiName(),
                                    downloadSpeed
                                )
                            )
                        })

                    //Check if login has been done before
                    if (loginSuccessfulWithSpeedZero)
                    //When download is successful, & login api has been called before successfully, call Speed Test Wifi Api
                        coroutineScope.launch {
                            callSetWifiSpeedTestApi(
                                wifiId = wifiId,
                                deviceId = deviceId,
                                speed = downloadSpeed!!.toDouble()
                            )
                        }
                    else
                    //When download is successful, & login api hasn't been called before, call Login wifi
                        coroutineScope.launch {
                            callWifiLogin(
                                wifiSsid = wifiSsid,
                                wifiId = wifiId,
                                averageSpeed = downloadSpeed!!.toDouble(),
                                deviceId = deviceId
                            )
                        }


                },
                onProgressReport = {

                },
                onErrorReport = {
                    //Start Service
                    ContextCompat.startForegroundService(
                        applicationContext,
                        Intent(applicationContext, WifiService::class.java).apply {
                            putExtra(
                                wifi_notification_key,
//                                    "No Internet Connection"
                                getString(R.string.no_internet_connection_label)
                            )
                        })

                    //When download is unsuccessful, try calling Speed Test or Login wifi with 0 speed

                    //Check if login has been done before
                    if (loginSuccessfulWithSpeedZero)
                    //When download is successful, & login api has been called before successfully, call Speed Test Wifi Api
                        coroutineScope.launch {
                            callSetWifiSpeedTestApi(
                                wifiId = wifiId,
                                deviceId = deviceId,
                                speed = 0.0
                            )
                        }
                    else
                    //When download is successful, & login api hasn't been called before, call Login wifi
                        coroutineScope.launch {
                            callWifiLogin(
                                wifiSsid = wifiSsid,
                                wifiId = wifiId,
                                averageSpeed = 0.0,
                                deviceId = deviceId
                            )
                        }
                }).startDownload(Constants.DOWNLOAD_LINK)
        }

    }
}