package com.ey.hotspot.ui.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.profile.fragment.model.ProfileResponse
import com.ey.hotspot.ui.profile.fragment.model.UpdateProfileRequest
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : BaseViewModel(application) {


    var firstName = ""
    var lastName = ""
    var mobileNo = ""
    var emailId = ""


    protected val _profileResponse = MutableLiveData<BaseResponse<ProfileResponse>>()
    val profileResponse: LiveData<BaseResponse<ProfileResponse>>
        get() = _profileResponse

    private val _updateProfileResponse = MutableLiveData<BaseResponse<Any>>()

    val updateProfileResponse: LiveData<BaseResponse<Any>>
        get() = _updateProfileResponse


    fun getProfileDetails() {

        coroutineScope.launch {
            DataProvider.getProfile(success = {

                _profileResponse.value = it
                setUPdata(it.data)
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
                }, error = {
                    _errorText.value = it.message
                }
            )
        }
    }




    private fun setUPdata(profileResponse: ProfileResponse) {

        firstName = profileResponse.firstname
        lastName = profileResponse.lastname
        emailId = profileResponse.email
        mobileNo= profileResponse.mobile_no

    }
}