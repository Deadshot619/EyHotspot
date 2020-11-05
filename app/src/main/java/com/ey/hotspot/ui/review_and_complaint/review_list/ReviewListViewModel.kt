package com.ey.hotspot.ui.review_and_complaint.review_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LocationReviews
import com.ey.hotspot.utils.constants.ReviewSortType
import kotlinx.coroutines.launch

class ReviewListViewModel(application: Application) : BaseViewModel(application) {

    private val _reviewList = MutableLiveData<BaseResponse<List<LocationReviews>>>()
    val reviewList: LiveData<BaseResponse<List<LocationReviews>>>
        get() = _reviewList

    init {
        getReviewsList(ReviewSortType.LATEST)
    }

    fun getReviewsList(value: ReviewSortType) {
        setDialogVisibility(true)
        coroutineScope.launch {
            DataProvider.getReviews(
                request = value,
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