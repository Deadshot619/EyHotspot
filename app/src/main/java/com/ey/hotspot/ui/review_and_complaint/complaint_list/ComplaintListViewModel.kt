package com.ey.hotspot.ui.review_and_complaint.complaint_list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.ComplaintsList
import kotlinx.coroutines.launch

class ComplaintListViewModel(application: Application) : BaseViewModel(application) {

    private val _complaintList = MutableLiveData<BaseResponse<List<ComplaintsList>>>()
    val complaintList: LiveData<BaseResponse<List<ComplaintsList>>>
        get() = _complaintList

    init {
        getComplaintsList()
    }

    private fun getComplaintsList() {
        coroutineScope.launch {
            DataProvider.getCompaints(
                success = {
                        _complaintList.value = it
                },
                error = {
                    checkError(it)
                }
            )
        }
    }

}