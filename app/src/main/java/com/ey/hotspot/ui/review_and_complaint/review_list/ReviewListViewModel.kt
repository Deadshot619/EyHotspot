package com.ey.hotspot.ui.review_and_complaint.review_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel

class ReviewListViewModel(application: Application) : BaseViewModel(application) {

    private val _reviewList = MutableLiveData<List<ReviewListModel>>(
        listOf(
            ReviewListModel(1, "D-Link", "Osama Bin Laden", 3.0, "9/11 hehehe"),
            ReviewListModel(2, "Pentagon", "George Bush", 3.0, "Jet fuel can't melt steel beams")
        )
    )
    val reviewList: LiveData<List<ReviewListModel>>
        get() = _reviewList
}