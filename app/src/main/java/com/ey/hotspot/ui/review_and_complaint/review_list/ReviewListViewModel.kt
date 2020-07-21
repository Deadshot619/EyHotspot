package com.ey.hotspot.ui.review_and_complaint.review_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.ReviewsList
import kotlinx.coroutines.launch

class ReviewListViewModel(application: Application) : BaseViewModel(application) {

    private val _reviewList = MutableLiveData<List<ReviewsList>>()
    val reviewList: LiveData<List<ReviewsList>>
        get() = _reviewList

    init {
        getReviewsList()
    }

    private fun getReviewsList() {
        coroutineScope.launch {
            DataProvider.getReviews(
                success = {
                    if (it.status)
                        _reviewList.value = it.data
                },
                error = {
                    checkError(it)
                }
            )
        }
    }
}