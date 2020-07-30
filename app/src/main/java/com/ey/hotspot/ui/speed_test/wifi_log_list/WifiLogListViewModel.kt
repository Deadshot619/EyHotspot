package com.ey.hotspot.ui.speed_test.wifi_log_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.WifiLogListRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.WifiLogListResponse
import com.ey.hotspot.utils.constants.getDeviceId
import kotlinx.coroutines.launch

class WifiLogListViewModel(application: Application) : BaseViewModel(application) {

    private val _wifiLogListResponse = MutableLiveData<BaseResponse<List<WifiLogListResponse>>>()
    val wifiLogListResponse: LiveData<BaseResponse<List<WifiLogListResponse>>>
        get() = _wifiLogListResponse

    val deviceId = getDeviceId()

    init {
        callWifiLogListResponse(deviceId)
    }


    private fun callWifiLogListResponse(deviceId: String) {
        val request = WifiLogListRequest(deviceId = deviceId)

        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.wifiLogList(
                request,
                success = {
                    _wifiLogListResponse.value = it
                    setDialogVisibility(false)
                },
                error = {
                    showToastFromViewModel(it.message)
                    setDialogVisibility(false)
                }
            )
        }

    }
}

