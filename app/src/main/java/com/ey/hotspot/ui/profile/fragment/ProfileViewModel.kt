package com.ey.hotspot.ui.profile.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.ui.profile.fragment.model.ProfileResponse
import com.ey.hotspot.ui.profile.fragment.model.Success
import com.ey.hotspot.ui.profile.updateprofile.model.UpdateProfileRequest
import com.ey.hotspot.ui.profile.updateprofile.model.UpdateProfileResponse
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : BaseViewModel(application) {


    var firstName = ""
    var lastName = ""
    var mobileNo = ""
    var emailId = ""


    protected val _profileResponse = MutableLiveData<ProfileResponse>()
    val profileResponse: LiveData<ProfileResponse>
        get() = _profileResponse

    private val _updateProfileResponse = MutableLiveData<UpdateProfileResponse>()

    val updateProfileResponse: LiveData<UpdateProfileResponse>
        get() = _updateProfileResponse


    fun getProfileDetails() {

        coroutineScope.launch {
            DataProvider.getProfile(success = {

                _profileResponse.value = it
                setUPdata(it)
            }, error = {
                _errorText.value = it.message
            })
        }
    }



    fun updateProfile(updateProfileRequest: UpdateProfileRequest) {

        coroutineScope.launch {

            DataProvider.updateProfile(
                request = updateProfileRequest,
                success = {

                    _updateProfileResponse.value = it
                    setUpUpdateData()
                }, error = {
                    _errorText.value = it.message
                }
            )
        }
    }

    private fun setUpUpdateData() {

    }


    private fun setUPdata(profileResponse: ProfileResponse) {

        firstName = profileResponse.success.firstname
        lastName = profileResponse.success.lastname
        emailId = profileResponse.success.email
        mobileNo= profileResponse.success.mobile_no

    }
}