package com.ey.hotspot.ui.speed_test.test_result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.response.ValidateWifiResponse
import com.ey.hotspot.utils.Event
import com.ey.hotspot.utils.SpeedTestUtils
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.extention_functions.convertBpsToMbps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class TestResultsViewModel(application: Application) : BaseViewModel(application) {

    private val _downloadSpeed = MutableLiveData<BigDecimal>()
    val downloadSpeed: LiveData<BigDecimal>
        get() = _downloadSpeed

    private val _downloadCompleted = MutableLiveData<Boolean>(false)
    val downloadCompleted: LiveData<Boolean>
        get() = _downloadCompleted

    private val _wifiData = MutableLiveData<ValidateWifiResponse>()
    val wifiData: LiveData<ValidateWifiResponse>
        get() = _wifiData

    private val _hideDataView = MutableLiveData<Boolean>()
    val hideDataView: LiveData<Boolean>
        get() = _hideDataView

    init {
        onCheckSpeedClick()
    }

    fun onCheckSpeedClick() {
        coroutineScope.launch {
            startDownload()
        }
    }

    //Method to set download speed value
    fun setSpeedValue(value: BigDecimal){
        _downloadSpeed.value = value
    }

    private suspend fun startDownload() {
        withContext(Dispatchers.IO) {
            SpeedTestUtils.calculateSpeed(
                {
                    _downloadSpeed.postValue(it?.transferRateBit?.convertBpsToMbps())
                    _downloadCompleted.postValue(true)
                }, {
                    _downloadSpeed.postValue(it?.transferRateBit?.convertBpsToMbps())
                    _downloadCompleted.postValue(false)
                }, {
                    _toastMessage.postValue(Event(it))
                    _downloadSpeed.postValue(0.toBigDecimal())
                    _downloadCompleted.postValue(true)
                }).startDownload(Constants.DOWNLOAD_LINK)
        }
    }


    fun setWifiData(wifiData: ValidateWifiResponse?, hideDataView: Boolean?){
        _wifiData.value = wifiData
        _hideDataView.value = hideDataView
    }
}