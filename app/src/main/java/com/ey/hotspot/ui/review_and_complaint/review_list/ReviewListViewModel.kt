package com.ey.hotspot.ui.review_and_complaint.review_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LocationReviews
import kotlinx.coroutines.launch

class ReviewListViewModel(application: Application) : BaseViewModel(application) {

    private val _reviewList = MutableLiveData<BaseResponse<List<LocationReviews>>>()
    val reviewList: LiveData<BaseResponse<List<LocationReviews>>>
        get() = _reviewList

    init {
        getReviewsList()
    }

    private fun getReviewsList() {
        setDialogVisibility(true)
        coroutineScope.launch {
            DataProvider.getReviews(
                success = {
                        _reviewList.value = it
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