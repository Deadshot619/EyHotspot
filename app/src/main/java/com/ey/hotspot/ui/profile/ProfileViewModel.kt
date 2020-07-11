package com.ey.hotspot.ui.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.profile.fragment.model.ProfileDataModel
import com.ey.hotspot.ui.profile.fragment.model.ProfileResponse
import com.ey.hotspot.ui.profile.fragment.model.UpdateProfileRequest
import com.ey.hotspot.utils.Event
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : BaseViewModel(application) {

    //Variable to hold profile data
    val profileData = MutableLiveData<ProfileDataModel>()

    private val _profileResponse = MutableLiveData<Event<BaseResponse<ProfileResponse>>>()
    val profileResponse: LiveData<Event<BaseResponse<ProfileResponse>>>
        get() = _profileResponse

    init {
        getProfileDetails()
    }


    //Method to retrieve Profile Details
    private fun getProfileDetails() {
        //Show dialog
        setDialogVisibility(true,  appInstance.getString(R.string.retrieving_profile_details_label))

        coroutineScope.launch {
            DataProvider.getProfile(success = {

                _profileResponse.value = Event(it)

                if (it.status)
                    profileData.value = ProfileDataModel(
                        firstName = it.data.firstname,
                        lastName = it.data.lastname,
                        mobileNo = it.data.mobile_no,
                        emailId = it.data.email
                    )

                //Hide dialog
                setDialogVisibility(false)
            }, error = {
                checkError(it)
            })
        }
    }


    //Method to update User Profile Details
    fun updateProfile(updateProfileRequest: UpdateProfileRequest) {
        setDialogVisibility(true, appInstance.getString(R.string.updating_profile_label))

        coroutineScope.launch {

            DataProvider.updateProfile(
                request = updateProfileRequest,
                success = {

                    _errorText.value = it.message


                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }
            )
        }
    }


}