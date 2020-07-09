package com.ey.hotspot.ui.profile.updateprofile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.ui.profile.updateprofile.model.UpdateProfileRequest
import com.ey.hotspot.ui.profile.updateprofile.model.UpdateProfileResponse
import kotlinx.coroutines.launch

class UpdateProfileViewModel(application: Application) : BaseViewModel(application) {


    private val _updateProfileResponse = MutableLiveData<UpdateProfileResponse>()

    val updateProfileResponse: LiveData<UpdateProfileResponse>
        get() = _updateProfileResponse


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
}