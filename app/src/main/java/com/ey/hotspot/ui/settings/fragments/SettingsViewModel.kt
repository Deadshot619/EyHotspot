package com.ey.hotspot.ui.settings.fragments

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.database.wifi_info.WifiInfoDatabase
import com.ey.hotspot.database.wifi_info.WifiInfoDatabaseDao
import com.ey.hotspot.database.wifi_info.WifiInformationTable
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.WifiLogoutRequest
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.getDeviceId
import com.ey.hotspot.utils.extention_functions.logoutUser
import com.ey.hotspot.utils.extention_functions.toServerFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SettingsViewModel (application: Application): BaseViewModel(application){
    private lateinit var wifiManager: WifiManager
    private lateinit var database: WifiInfoDatabaseDao
    private val DEVICE_ID = getDeviceId()

    val currentLanguage = MutableLiveData<String>()

    init {
        currentLanguage.value = if (HotSpotApp.prefs!!.getLanguage() == Constants.ENGLISH_LANG)
            application.getString(R.string.english)
        else
            application.getString(R.string.arabic)

        //Get Wifi Manager
        wifiManager = appInstance.getSystemService(Context.WIFI_SERVICE) as WifiManager

        //get instance of database
        database = WifiInfoDatabase.getInstance(appInstance).wifiInfoDatabaseDao

    }


    fun wifiLogout(){
        coroutineScope.launch {
            getLastInsertedData()
        }
    }


    suspend fun getLastInsertedData(){
        withContext(Dispatchers.IO){
            //Get wifi login data from db
            val data = database.getLastInsertedData()

            /*
             *  Check whether data is present, if it is then check whether it is synced, If yes then
             */
            if (data.isNotEmpty() && !data[0].synced)
                callWifiLogout(data[0].id, data[0].wifiId, DEVICE_ID, Calendar.getInstance().time)

            updateLogoutTimeInDb(data)

            appInstance.logoutUser()
        }
    }

    /*
     *  Method to Update Logout Time in DB.
     *  Will be called when wifi is disconnected/lost
     */
    private suspend fun updateLogoutTimeInDb(lastInsertedData: List<WifiInformationTable>) {
        //Update data in table
        withContext(Dispatchers.IO) {
//            val lastInsertedData: List<WifiInformationTable> = database.getLastInsertedData()

            if (lastInsertedData.isNotEmpty() && lastInsertedData[0].disconnectedOn.toString() == "null") {
                database.updateWifiInfoData(
                    id = lastInsertedData[0].id,
                    disconnectedOn = Calendar.getInstance()
                )


                /*
                if (_currentlyInsertedDataId >= 0L) {   //Update data only if key is >= 0
                    val success = database.updateWifiInfoData(
                        id = _currentlyInsertedDataId,
                        disconnectedOn = Calendar.getInstance()
                    )
                */
            }
        }
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

        setDialogVisibilityPost(true)
        DataProvider.wifiLogout(
            request = request,
            success = {
                if (it.status) {
                    //If logout is successful, then update sync status of the data in DB
                    coroutineScope.launch {
                        updateSyncStatusOfDataInDb(dbId = dbId, syncStatus = it.status)
                    }
                }

                setDialogVisibilityPost(false)
            },
            error = {
            }
        )
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
}