package com.ey.hotspot.ui.speed_test.speed_test_fragmet

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.database.wifi_info.WifiInfoDatabase
import com.ey.hotspot.database.wifi_info.WifiInfoDatabaseDao
import com.ey.hotspot.database.wifi_info.WifiInformationTable
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.SpeedTestRequest
import com.ey.hotspot.network.request.ValidateWifiRequest
import com.ey.hotspot.network.request.WifiLoginRequest
import com.ey.hotspot.network.response.ValidateWifiResponse
import com.ey.hotspot.service.WifiService
import com.ey.hotspot.utils.SpeedTestUtils
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.SpeedTestModes
import com.ey.hotspot.utils.constants.getDeviceId
import com.ey.hotspot.utils.extention_functions.convertBpsToMbps
import com.ey.hotspot.utils.extention_functions.extractWifiName
import com.ey.hotspot.utils.wifi_notification_key
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SpeedTestFragmentViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var wifiManager: WifiManager
    private lateinit var database: WifiInfoDatabaseDao
    private val DEVICE_ID = getDeviceId()

    //Variable to store whether the connected wifi is Our open wifi
    private var isItOurWifi = false
    private var validateWifiSuccessful =
        false  //True, if wifi is validated successfully, else false

    private var loginSuccessfulWithSpeedZero: Boolean = false
    private var requireApiCall: Boolean = true

    init {
        //Get Wifi Manager
        wifiManager = appInstance.getSystemService(Context.WIFI_SERVICE) as WifiManager

        //get instance of database
        database = WifiInfoDatabase.getInstance(appInstance).wifiInfoDatabaseDao
    }




    private val _wifiData = MutableLiveData<ValidateWifiResponse>()
    val wifiData: LiveData<ValidateWifiResponse>
        get() = _wifiData

    private val _hideDataView = MutableLiveData<Boolean>(true)
    val hideDataView: LiveData<Boolean>
        get() = _hideDataView

    //Method to verify Wifi Hotspot
    fun verifyHotspot(wifiSsid: String, lat: Double, lng: Double) {
        val request = ValidateWifiRequest(
            wifi_name = wifiSsid,
            lat = lat,
            lng = lng
        )

        setDialogVisibility(true)
        coroutineScope.launch {
            DataProvider.validateWifi(
                request = request,
                success = {
//                    showToastFromViewModel(it.message)
                    if (it.status)
                        _wifiData.value = it.data

                    _hideDataView.value = !it.status

                },
                error = {
                    checkError(it)
                }
            )
        }
    }



    /*
   * Method to validate if the wifi is company's WiFi. If yes, then Calculate wifi download speed
   */
    fun validateWifi(wifiSsid: String, lat: Double, lng: Double) {
        val request = ValidateWifiRequest(wifiSsid, lat = lat, lng = lng)

        setDialogVisibility(true)


        //Set this variable to false
        loginSuccessfulWithSpeedZero = false

        coroutineScope.launch {

            DataProvider.validateWifi(
                request = request,
                success = {


                    if (it.status) {
                        validateWifiSuccessful = true
//                    _currentWifiId = it.data.id

                        coroutineScope.launch {
                            //Call login api with 0.0 average speed & calculate speed

                            getLastInsertedDataForLogin(it.data, wifiSsid)
                        }
                    } else {
                        validateWifiSuccessful = false
                        ContextCompat.startForegroundService(
                            appInstance,
                            Intent(appInstance, WifiService::class.java).apply {
                                putExtra(
                                    wifi_notification_key,
                                    appInstance.getString(R.string.wifi_not_validated_label)
                                )
                            })

                        _hideDataView.value = !it.status
                    }
                },
                error = {
                    validateWifiSuccessful = false
                    ContextCompat.startForegroundService(
                        appInstance,
                        Intent(appInstance, WifiService::class.java).apply {
                            putExtra(
                                wifi_notification_key,
                                appInstance.getString(R.string.wifi_not_validated_label)
                            )
                        })

                    _hideDataView.value = true
                }
            )
        }
    }

    private suspend fun getLastInsertedDataForLogin(validateData: ValidateWifiResponse, wifiSsid: String) {
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
                    if (HotSpotApp.prefs!!.getAccessToken() == data[0].accessToken && (data[0].disconnectedOn.toString() == "null" || data[0].wifiSsid == wifiSsid))
                        requireApiCall = false
                    else
                        requireApiCall = true
                } else {    //Skipped user
                    //If user already has access token stored in db as null, that means the wifi is logged in for current skipped user
                    //So if token is null, set this to false, else true
                    if (data[0].accessToken.isNullOrEmpty() && (data[0].disconnectedOn.toString() == "null" || data[0].wifiSsid == wifiSsid))
                        requireApiCall = false
                    else
                        requireApiCall = true
                }
            }

            //If wifi is already logged in, then don't call Wifi Login Api & set the flag to true
            if (requireApiCall)
                callWifiLogin(wifiId = validateData.id, deviceId = DEVICE_ID, averageSpeed = 0.0)
            else{
                loginSuccessfulWithSpeedZero = true

                //Set data to view
                _wifiData.postValue(validateData)
                setDialogVisibilityPost(false)
                _hideDataView.postValue(requireApiCall)
            }

            //Calculate speed anyways
            /*calculateSpeed(
                wifiId = validateData.id,
                deviceId = DEVICE_ID
            )*/
        }
    }

    //Method to call WifiLogin Api
    private suspend fun callWifiLogin(wifiId: Int, averageSpeed: Double, deviceId: String) {
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
                        deleteAndInsertData(wifiId = wifiId, averageSpeed = averageSpeed)
                    }

                    //Set data to view
                    _wifiData.postValue(it.data)
                    setDialogVisibilityPost(false)

                } else {
                    //Set login status to false as login wasn't successful
                    loginSuccessfulWithSpeedZero = false
                }

                _hideDataView.postValue(!it.status)
            },
            error = {
                //Set login status to false as login wasn't successful
                loginSuccessfulWithSpeedZero = false
            }
        )
    }

    /*
     *  Method to calculate speed
     */
    private suspend fun calculateSpeed(wifiId: Int, deviceId: String) {
        withContext(Dispatchers.IO) {
            SpeedTestUtils.calculateSpeed(
                onCompletedReport = {       //When speed test is completed successfully
                    //get download speed
                    val downloadSpeed = it?.transferRateBit?.convertBpsToMbps()

                    //Start Service
                    ContextCompat.startForegroundService(
                        appInstance,
                        Intent(appInstance, WifiService::class.java).apply {
                            putExtra(
                                wifi_notification_key,
                                String.format(
                                    appInstance.getString(R.string.calculated_wifi_speed_label),
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
                        appInstance,
                        Intent(appInstance, WifiService::class.java).apply {
                            putExtra(
                                wifi_notification_key,
//                                    "No Internet Connection"
                                appInstance.getString(R.string.no_internet_connection_label)
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
                                wifiId = wifiId,
                                averageSpeed = 0.0,
                                deviceId = deviceId
                            )
                        }
                }).startDownload(Constants.DOWNLOAD_LINK)
        }

    }

    /*
        This method will be called after Login is done & Speed Test has been completed/Failed .
     */
    private suspend fun callSetWifiSpeedTestApi(wifiId: Int, deviceId: String, speed: Double) {
        val request =
            SpeedTestRequest(wifi_id = wifiId, device_id = deviceId, average_speed = speed, mode = SpeedTestModes.BACKGROUND.value)

        DataProvider.wifiSpeedTest(
            request = request,
            success = {
            },
            error = {
            }
        )
    }

    /*
 *  This method will delete all data from DB then insert a new row.
 *  This will be called when user's wifi data has been successfully logged in on Server.
 */
    private suspend fun deleteAndInsertData(wifiId: Int, averageSpeed: Double) {
        withContext(Dispatchers.IO) {
            synchronized(this) {
                //First delete data from DB
                database.deleteAllDataFromDb()

                //Then insert new data  from DB
                database.insert(
                    WifiInformationTable(
                        wifiSsid = wifiManager.connectionInfo.ssid.extractWifiName(),
                        connectedOn = Calendar.getInstance(),
                        downloadSpeed = averageSpeed.toString(),
                        wifiId = wifiId,
                        accessToken = HotSpotApp.prefs?.getAccessToken()
                    )
                )
            }
        }
    }

}