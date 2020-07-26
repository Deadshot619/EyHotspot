package com.ey.hotspot.ui.speed_test.speed_test_fragmet

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.ValidateWifiRequest
import com.ey.hotspot.network.response.ValidateWifiResponse
import kotlinx.coroutines.launch

class SpeedTestFragmentViewModel(application: Application) : BaseViewModel(application) {

    private val _wifiData = MutableLiveData<ValidateWifiResponse>()
    val wifiData: LiveData<ValidateWifiResponse>
        get() = _wifiData


    fun verifyHotspot(lat: Double, lng: Double){
        val request = ValidateWifiRequest(
            wifi_name = "jio",
            lat =  lat,
            lng = lng
        )

        setDialogVisibility(true)
        coroutineScope.launch {
            DataProvider.validateWifi(
                request = request,
                success = {
                    showToastFromViewModel(it.message)
                    setDialogVisibility(false)
                },
                error = {
                    checkError(it)
                }
            )
        }
    }
}