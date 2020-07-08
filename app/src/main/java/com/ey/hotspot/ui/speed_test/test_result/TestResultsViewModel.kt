package com.ey.hotspot.ui.speed_test.test_result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.utils.SpeedTestUtils
import com.ey.hotspot.utils.convertBpsToMbps
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class TestResultsViewModel(application: Application) : BaseViewModel(application) {

    private val _downloadSpeed = MutableLiveData<BigDecimal>()
    val downloadSpeed: LiveData<BigDecimal>
        get() = _downloadSpeed

    private val _uploadSpeed = MutableLiveData<BigDecimal>()
    val uploadSpeed: LiveData<BigDecimal>
        get() = _uploadSpeed

    private val _errorText = MutableLiveData<String>()
    val errorText: LiveData<String>
        get() = _errorText

    init {
        onCheckSpeedClick()
    }

    fun onCheckSpeedClick() {
        coroutineScope.launch {
            startDownload()
//            startUpload()
        }
    }

    private suspend fun startDownload() {
        withContext(Dispatchers.IO) {
            SpeedTestUtils.calculateSpeed(
                {
                    _downloadSpeed.postValue(it?.transferRateBit?.convertBpsToMbps())
                }, {
                    _downloadSpeed.postValue(it?.transferRateBit?.convertBpsToMbps())
                }, {
                    _errorText.postValue(it)
                }).startDownload("http://ipv4.ikoula.testdebit.info/1M.iso")
        }
    }

    private suspend fun startUpload() {
        withContext(Dispatchers.IO) {
            SpeedTestUtils.calculateSpeed(
                {
                    _uploadSpeed.postValue(it?.transferRateBit?.convertBpsToMbps())
                }, {
                    _uploadSpeed.postValue(it?.transferRateBit?.convertBpsToMbps())
                }, {
                    _errorText.postValue(it)
                }).startUpload("http://ipv4.ikoula.testdebit.info/", 1000000);
        }
    }
}