package com.ey.hotspot.ui.speed_test.raise_complaint

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.AddComplaintRequest
import com.ey.hotspot.network.response.ComplaintIssuesTypes
import com.ey.hotspot.utils.Event
import kotlinx.coroutines.launch

class RaiseComplaintViewModel(application: Application) : BaseViewModel(application) {

    //Variable to store Complaint Data
    val raiseComplaintData = MutableLiveData(RaiseComplaintModel())

    //Variable to store IssueTypes
    private val _issueTypes = MutableLiveData<ComplaintIssuesTypes>()
    val issuesTypes: LiveData<ComplaintIssuesTypes>
        get() = _issueTypes

    private val _goBack = MutableLiveData<Event<Boolean>>()
    val goBack: LiveData<Event<Boolean>>
        get() = _goBack

    init {
        getIssuesTypes()
    }

    //Method to get Issues type spinner data
    private fun getIssuesTypes() {
        coroutineScope.launch {
            DataProvider.getComplaintsIssuesTypes(
                {
                    if (it.status)
                        _issueTypes.value = it.data
                    else
                        showToastFromViewModel(it.message)
                },
                {
                    checkError(it)
                }
            )
        }
    }


    /**
     * Method to add Complaint
     */
    fun addComplaint() {
        val request = AddComplaintRequest(
            locationId = raiseComplaintData.value?.id ?: -1,
            issueType = raiseComplaintData.value?.issueType ?: "",
            complaint = raiseComplaintData.value?.feedback ?: ""
        )

        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.addComplaints(
                request = request,
                success = {
                    showToastFromViewModel(it.message)
                    setDialogVisibility(false)

                    if (it.status)
                        _goBack.value = Event(true)
                },
                error ={
                    checkError(it)
                }
            )
        }
    }
}