package com.ey.hotspot.ui.speed_test.wifi_log_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.database.WifiInfoDatabase
import com.ey.hotspot.database.WifiInfoDatabaseDao
import com.ey.hotspot.database.WifiInformationTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class WifiLogListViewModel(application: Application) : BaseViewModel(application) {

    var database: WifiInfoDatabaseDao = WifiInfoDatabase.getInstance(application).wifiInfoDatabaseDao

    private val _wifiLogList = MutableLiveData<List<WifiInformationTable>>()
    val wifiLogList: LiveData<List<WifiInformationTable>>
        get() = _wifiLogList

    init {
        getDataFromDb()
    }

    private fun getDataFromDb(){
        coroutineScope.launch {
            retrieveDataFromDb(database)
        }
    }

    private suspend fun retrieveDataFromDb(wifiInfoDatabaseDao: WifiInfoDatabaseDao) {
        withContext(Dispatchers.IO){
            _wifiLogList.postValue(wifiInfoDatabaseDao.getAllWifiInfoData())
        }
    }
}

