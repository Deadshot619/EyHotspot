package com.ey.hotspot.ui.speed_test.rate_wifi

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import kotlinx.coroutines.launch

class RateWifiViewModel(application: Application) : BaseViewModel(application) {

    val rateWifiData = MutableLiveData(RateWifiModel())

    fun addReview() {
        coroutineScope.launch {
            DataProvider.addReviews(
                {
                    showToastFromViewModel(it.message)
                },
                {
                    checkError(it)
                }
            )
        }
    }
}