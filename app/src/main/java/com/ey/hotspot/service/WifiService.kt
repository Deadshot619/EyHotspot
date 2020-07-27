package com.ey.hotspot.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.ey.hotspot.R
import com.ey.hotspot.database.wifi_info.WifiInfoDatabase
import com.ey.hotspot.database.wifi_info.WifiInfoDatabaseDao
import com.ey.hotspot.database.wifi_info.WifiInformationTable
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.ValidateWifiRequest
import com.ey.hotspot.network.request.WifiLoginRequest
import com.ey.hotspot.network.request.WifiLogoutRequest
import com.ey.hotspot.utils.CHANNEL_ID
import com.ey.hotspot.utils.SpeedTestUtils
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.extention_functions.convertBpsToMbps
import com.ey.hotspot.utils.extention_functions.extractWifiName
import com.ey.hotspot.utils.extention_functions.getUserLocation
import com.ey.hotspot.utils.getNotification
import com.ey.hotspot.utils.wifi_notification_key
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WifiService : Service() {
    companion object {
        //Variable to indicate whether the service is running
        private var _isRunning = false
        val isRunning: Boolean
            get() = _isRunning
    }

    private lateinit var connec: ConnectivityManager
    private lateinit var wifiManager: WifiManager
    private lateinit var database: WifiInfoDatabaseDao

    //Current WiFi service id
    private val WIFI_SERVICE_ID = 1

    //Holds value of currently inserted DB data
    private var _currentlyInsertedDataId = -1L
    private var _currentWifiId = 0
    private var TEMP_DEVICE_ID = "bleh"


    //Variable to store whether the connected wifi is Our open wifi
    private var isItOurWifi = false
    private var validateWifiSuccessful =
        false  //True, if wifi is validated successfully, else false

    override fun onCreate() {
        super.onCreate()

        //indicate Service is running
        _isRunning = true

        //Get connectivity Manager
        connec = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //Get Wifi Manager
        wifiManager = applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val networkRequestWiFi: NetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        //get instance of database
        database = WifiInfoDatabase.getInstance(application).wifiInfoDatabaseDao


        connec.registerNetworkCallback(networkRequestWiFi, networkCallbackWiFi)
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
                    input,
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
    }


    //Network callback for WIFI
    private var networkCallbackWiFi = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network?) {
            //TODO 11/7/2020 : Remove this, once live
            /*Toast.makeText(
                applicationContext,
                getString(R.string.wifi_disconnected_label),
                Toast.LENGTH_SHORT
            ).show()*/

            //Start Service
            ContextCompat.startForegroundService(
                applicationContext,
                Intent(applicationContext, WifiService::class.java).apply {
                    putExtra(
                        wifi_notification_key,
//                                    "No Internet Connection"
                        getString(R.string.wifi_disconnected_label)
                    )
                }
            )

            //Update data in table
            CoroutineScope(Dispatchers.IO).launch {
                if (_currentlyInsertedDataId >= 0L) {   //Update data only if key is >= 0
                    val success = database.updateWifiInfoData(
                        id = _currentlyInsertedDataId,
                        disconnectedOn = Calendar.getInstance()
                    )

                    if (success == 1)       //If row updated successfully, then change current id
                        _currentlyInsertedDataId = -1
                }
            }
        }

        override fun onAvailable(network: Network?) {
            synchronized(this){
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = database.getLastInsertedData()
                        if (data.isNotEmpty() && !data[0].synced)
                            callWifiLogout(wifiId = data[0].wifiId, deviceId = TEMP_DEVICE_ID)
                    }
            }

            //Wifi Ssid
            val wifiSsid = wifiManager.connectionInfo.ssid.extractWifiName()
            if (!wifiSsid.contains(Constants.UNKNOWN_SSID)) {    //If the wifi is "unknown ssid", then skip it
                /*
                 *  Check if the wifi name is present in wifi keywords
                 */
                isItOurWifi = /*WIFI_KEYWORDS?.contains(wifiSsid) ?: false*/ true   //TODO 27/07/2020: Remove True, when live
            } else {
                isItOurWifi = false
            }

            //If the wifi contains some special keywords then validate current wifi
            if (isItOurWifi) {
                applicationContext.getUserLocation { lat, lng ->
                    if (lat != null && lng != null) {   //If Location is available
                        val request = ValidateWifiRequest(wifiSsid, lat = lat, lng = lng)
                        CoroutineScope(Dispatchers.Main).launch {
                            DataProvider.validateWifi(
                                request = request,
                                success = {
                                    if (it.status) {
                                        validateWifiSuccessful = true
                                        _currentWifiId = it.data.id
                                        calculateSpeed(wifiId = it.data.id, deviceId = TEMP_DEVICE_ID)
                                    } else
                                        validateWifiSuccessful = false
                                },
                                error = {
                                    validateWifiSuccessful = false
                                }
                            )
                        }
                    } /*else
//                        mViewModel.verifyHotspot(wifiSSid, Constants.LATITUDE, Constants.LONGITUDE)
                }*/
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
            }
        }
    }

    //Method to call WifiLogin Api
    private fun callWifiLogin(wifiId: Int, averageSpeed: Double, deviceId: String) {
        val request = WifiLoginRequest(
            wifi_id = wifiId,
            average_speed = averageSpeed.toInt(),
            device_id = deviceId
        )

        CoroutineScope(Dispatchers.Main).launch {
            DataProvider.wifiLogin(
                request = request,
                success = {
                    if (it.status)
                    //Delete & Insert data into table
                        CoroutineScope(Dispatchers.IO).launch {
                            synchronized(this){
                                //First delete data
                                database.deleteAllDataFromDb()

                                //Then insert new data
                                _currentlyInsertedDataId = database.insert(
                                    WifiInformationTable(
                                        wifiSsid = wifiManager.connectionInfo.ssid.extractWifiName(),
                                        connectedOn = Calendar.getInstance(),
                                        downloadSpeed = averageSpeed.toString(),
                                        wifiId = wifiId
                                    )
                                )
                            }
                        }
                },
                error = {
                }
            )
        }
    }

    //Method to call WifiLogout Api
    private fun callWifiLogout(wifiId: Int, deviceId: String){
        val request = WifiLogoutRequest(
            wifi_id = wifiId,
            device_id = deviceId
        )

        CoroutineScope(Dispatchers.Main).launch {
            DataProvider.wifiLogout(
                request = request,
                success = {
                },
                error = {
                }
            )
        }
    }

    //Method to calculate speed
    fun calculateSpeed(wifiId: Int, deviceId: String){
        //TODO("Check for internet connection first")
        CoroutineScope(Dispatchers.IO).launch {
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


                    //Login wifi
                    CoroutineScope(Dispatchers.Main).launch {
                        callWifiLogin(wifiId = wifiId, averageSpeed = downloadSpeed!!.toDouble(), deviceId = TEMP_DEVICE_ID)
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

                    //Insert data into table
/*                    CoroutineScope(Dispatchers.IO).launch {
                        _currentlyInsertedDataId = database.insert(
                            WifiInformationTable(
                                wifiSsid = wifiManager.connectionInfo.ssid,
                                connectedOn = Calendar.getInstance(),
                                downloadSpeed = "0",
                                wifiId = wifiId
                            )
                        )
                    }*/
                }).startDownload(Constants.DOWNLOAD_LINK)
        }

    }
}