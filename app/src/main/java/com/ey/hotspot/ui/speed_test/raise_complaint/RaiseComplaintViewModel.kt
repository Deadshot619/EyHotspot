package com.ey.hotspot.ui.speed_test.raise_complaint

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.AddComplaintRequest
import com.ey.hotspot.network.response.ComplaintIssuesTypes
import com.ey.hotspot.network.response.Type
import com.ey.hotspot.utils.Event
import com.ey.hotspot.utils.constants.Constants
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
        setDialogVisibility(true)
        coroutineScope.launch {
            DataProvider.getComplaintsIssuesTypes(
                {
                    if (it.status) {
                        val langType = HotSpotApp.prefs!!.getLanguage()
                        if (langType == Constants.ARABIC_LANG) {
                            _issueTypes.value = ComplaintIssuesTypes(
                                mutableListOf(
                                    Type(
                                        "0",
                                       "حدد نوع المشكلة"
                                    )
                                ).apply {
                                    addAll(it.data.types)
                                }
                            )
                        }
                        else{
                            _issueTypes.value = ComplaintIssuesTypes(
                                mutableListOf(
                                    Type(
                                        "0",
                                        "Select issue type"
                                    )
                                ).apply {
                                    addAll(it.data.types)
                                }
                            )
                        }
                    }
                    else
                        showToastFromViewModel(it.message)
                    setDialogVisibility(false)
                },
                {
                    checkError(it)
                    setDialogVisibility(false)
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