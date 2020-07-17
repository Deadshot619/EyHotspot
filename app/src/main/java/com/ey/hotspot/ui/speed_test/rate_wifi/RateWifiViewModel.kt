package com.ey.hotspot.ui.speed_test.rate_wifi

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.AddReviewRequest
import com.ey.hotspot.utils.Event
import kotlinx.coroutines.launch

class RateWifiViewModel(application: Application) : BaseViewModel(application) {

    val rateWifiData = MutableLiveData(RateWifiModel())

    private val _goBack = MutableLiveData<Event<Boolean>>()
    val goBack: LiveData<Event<Boolean>>
        get() = _goBack


    fun addReview() {
        val request = AddReviewRequest(
            locationId = rateWifiData.value?.id ?: -1,
            rating = rateWifiData.value?.rating?.toInt() ?: -1,
            description = rateWifiData.value?.feedback ?: ""
        )

        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.addReviews(
                request,
                {
                    showToastFromViewModel(it.message)
                    setDialogVisibility(false)

                    if (it.status)
                        _goBack.value = Event(true)
                },
                {
                    checkError(it)
                }
            )
        }
    }
}