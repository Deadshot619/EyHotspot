package com.ey.hotspot.ui.review_and_complaint

import android.app.Application
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import kotlinx.coroutines.launch

class ReviewAndComplainViewModel(application: Application) : BaseViewModel(application) {


    init {
        getReviewsAndComplaints()
    }

    private fun getReviewsAndComplaints() {
        coroutineScope.launch {
            DataProvider.getReviewsAndComplaints(
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