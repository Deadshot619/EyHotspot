package com.ey.hotspot.ui.review_and_complaint.reviews

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.GetLocationReviewsRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.ReviewsList
import com.ey.hotspot.ui.speed_test.rate_wifi.RateWifiModel
import kotlinx.coroutines.launch

class ReviewsViewModel(application: Application) : BaseViewModel(application) {

    val rateWifiData = MutableLiveData(RateWifiModel())

    private val _reviewListResponse = MutableLiveData<BaseResponse<ReviewsList>>()
    val reviewListResponse: LiveData<BaseResponse<ReviewsList>>
        get() = _reviewListResponse

    fun getReviewsList() {
        val request = GetLocationReviewsRequest(
            locationId = rateWifiData.value?.id ?: -1
        )

        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.getLocationReviews(
                request = request,
                success = {

                    _reviewListResponse.value = it

                    setDialogVisibility(false)
                },
                error = {
                    checkError(it)
                    setDialogVisibility(false)
                }
            )
        }
    }

}