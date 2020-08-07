package com.ey.hotspot.ui.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.UpdateProfileRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.CoutryCode
import com.ey.hotspot.network.response.ProfileResponse
import com.ey.hotspot.network.response.UpdateProfileResponse
import com.ey.hotspot.ui.profile.fragment.model.ProfileDataModel
import com.ey.hotspot.utils.Event
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : BaseViewModel(application) {

    //Variable to hold profile data
    val profileData = MutableLiveData<ProfileDataModel>()

    private val _profileResponse = MutableLiveData<Event<BaseResponse<ProfileResponse>>>()
    val profileResponse: LiveData<Event<BaseResponse<ProfileResponse>>>
        get() = _profileResponse

    //This variable will handle Profile Errors
    private val _profileError = MutableLiveData<Event<UpdateProfileResponse?>>()
    val profileError: LiveData<Event<UpdateProfileResponse?>>
        get() = _profileError

    //Country Codes
    private val _getCoutryCodeList = MutableLiveData<Event<BaseResponse<CoutryCode>>>()
    val getCountryCodeList: LiveData<Event<BaseResponse<CoutryCode>>>
        get() = _getCoutryCodeList

    private val _emailChange = MutableLiveData<Event<UpdateProfileResponse>>()
    val emailChange: LiveData<Event<UpdateProfileResponse>>
        get() = _emailChange

    init {
        if (HotSpotApp.prefs?.getAppLoggedInStatus()!!)
            getCountryCodeList()
//            getProfileDetails()
    }


    //Method to retrieve Profile Details
    private fun getProfileDetails() {
        //Show dialog
        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.getProfile(success = {

                _profileResponse.value = Event(it)

                if (it.status)
                    profileData.value = ProfileDataModel(
                        firstName = it.data.firstname?.trim() ?: "",
                        lastName = it.data.lastname?.trim() ?: "",
                        mobileNo = it.data.mobile_no ?: "",
                        emailId = it.data.email ?: "",
                        countryCode = it.data.country_code
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
        setDialogVisibility(true)

        coroutineScope.launch {

            DataProvider.updateProfile(
                request = updateProfileRequest,
                success = {

                    showToastFromViewModel(it.message)

                    if (!it.status)
                        _profileError.value = Event(it.data)

                    //If email is changed successfully
                    if (it.status && it.data.email_change)
                        _emailChange.value = Event(it.data)

                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }
            )
        }
    }

    //Method to get Country code list
    private fun getCountryCodeList() {
        setDialogVisibility(true)
        coroutineScope.launch {

            DataProvider.getCountryCode(
                success = {
                    _getCoutryCodeList.value = Event(it)
                    if (it.status)
                        getProfileDetails()
                    else
                        setDialogVisibility(false)

                },
                error = {
                    checkError(it)
                }
            )
        }
    }


}