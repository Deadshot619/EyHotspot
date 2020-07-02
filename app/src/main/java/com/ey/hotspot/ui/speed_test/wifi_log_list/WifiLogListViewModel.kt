package com.ey.hotspot.ui.speed_test.wifi_log_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel

class WifiLogListViewModel(application: Application) : BaseViewModel(application) {

    private val _wifiLogList = MutableLiveData<List<WifiLogListModel>>(
        listOf(
            WifiLogListModel(1, "09-11-2001", "Osama Bin Laden"),
            WifiLogListModel(2, "69-69-0420", "George Bush")
        )

    )
    val wifiLogList: LiveData<List<WifiLogListModel>>
        get() = _wifiLogList


}