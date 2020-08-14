package com.ey.hotspot.ui.speed_test.test_result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.SpeedTestRequest
import com.ey.hotspot.network.response.ValidateWifiResponse
import com.ey.hotspot.utils.SpeedTestUtils
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.getDeviceId
import com.ey.hotspot.utils.extention_functions.convertBpsToMbps
import com.ey.hotspot.utils.extention_functions.parseToDouble
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
                    val calculatedSpeed = it?.transferRateBit?.convertBpsToMbps()
                    _downloadSpeed.postValue(calculatedSpeed)
                    _downloadCompleted.postValue(true)

                    wifiData.value?.let { data ->
                        if (data.id > 0)
                            setWifiSpeedTestData(wifiId = data.id, deviceId = getDeviceId(), speed = calculatedSpeed.toString().parseToDouble())
                    }

                }, {
                    _downloadSpeed.postValue(it?.transferRateBit?.convertBpsToMbps())
                    _downloadCompleted.postValue(false)
                }, {
//                    _toastMessage.postValue(Event(appInstance.getString(R.string.error_while_calculating_download_speed)))
                    _downloadSpeed.postValue(0.toBigDecimal())
                    _downloadCompleted.postValue(true)

                    wifiData.value?.let { data ->
                        if (data.id > 0)
                            setWifiSpeedTestData(wifiId = data.id, deviceId = getDeviceId(), speed = 0.0)
                    }
                }).startDownload(Constants.DOWNLOAD_LINK)
        }
    }

    //Method to set data which sets data to views
    fun setWifiData(wifiData: ValidateWifiResponse?, hideDataView: Boolean?){
        _wifiData.value = wifiData
        _hideDataView.value = hideDataView
    }

    //Method to set Wifi Speed Test Data on server
    private fun setWifiSpeedTestData(wifiId: Int, deviceId: String, speed: Double){
        val request = SpeedTestRequest(wifi_id = wifiId, device_id = deviceId, average_speed = speed)

//        setDialogVisibility(true)
            coroutineScope.launch {
            DataProvider.wifiSpeedTest(
                request = request,
                success = {
                    setDialogVisibility(false)
                },
                error = {
                    checkError(it)
                }
            )
        }
    }
}