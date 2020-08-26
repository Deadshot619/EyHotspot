package com.ey.hotspot.utils.wifi_logs_utils

import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.database.wifi_info.WifiInfoDatabaseDao
import com.ey.hotspot.network.response.ValidateWifiResponse
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.getDeviceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

object WifiLogUtils {

    suspend fun getLastInsertedDataForLogin(
//        requireApiCall: Boolean,
        wifiSsid: String,
        database: WifiInfoDatabaseDao,
        validateData: ValidateWifiResponse,
        callWifiLogin: (wifiSsid: String, wifiId: Int, deviceId: String, averageSpeed: Double) -> Unit,
        calculateSpeed: (wifiSsid: String, wifiId: Int, deviceId: String) -> Unit,
        wifiLoginNotRequired: (validateData: ValidateWifiResponse) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            synchronized(this) {
                val DEVICE_ID = getDeviceId()

                //Set this variable to true
                var requireApiCall = true

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
                if (requireApiCall) {
                    callWifiLogin(
                        wifiSsid,
                        validateData.id,
                        DEVICE_ID,
                        0.0
                    )

                    //Calculate speed anyways
                    calculateSpeed(
                        wifiSsid,
                        validateData.id,
                        DEVICE_ID
                    )
                } else {
//                    loginSuccessfulWithSpeedZero = true
                    wifiLoginNotRequired(validateData)
                }
            }

        }
    }
}