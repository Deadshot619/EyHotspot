package com.ey.hotspot.ui.speed_test.raise_complaint

import android.app.Application
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import kotlinx.coroutines.launch

class RaiseComplaintViewModel(application: Application) : BaseViewModel(application) {

    init {
        getIssuesTypes()
    }

    //Method to get Issues type spinner data
    private fun getIssuesTypes() {
        coroutineScope.launch {
            DataProvider.getComplaintsIssuesTypes(
                {
                    showToastFromViewModel(it.message)
                },
                {
                    checkError(it)
                }
            )
        }
    }


    private fun addComplaint() {
        coroutineScope.launch {
            DataProvider.addComplaints(
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