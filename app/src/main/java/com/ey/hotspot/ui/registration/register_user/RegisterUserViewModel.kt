package com.ey.hotspot.ui.registration.register_user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.UpdateProfileResponse
import com.ey.hotspot.ui.registration.register_user.model.RegistrationResponse
import com.ey.hotspot.utils.Event
import kotlinx.coroutines.launch

class RegisterUserViewModel(application: Application) : BaseViewModel(application) {

    var firstName = ""
    var lastName = ""
    var emailId = ""
    var password = ""
    var confirmPassword = ""
    var mobileNumber = ""
    var coutrycode = ""


    private val _registrationResponse = MutableLiveData<Event<BaseResponse<RegistrationResponse>>>()
    val registrationResponse: LiveData<Event<BaseResponse<RegistrationResponse>>>
        get() = _registrationResponse

    //This variable will handle Profile Errors
    private val _registrationError = MutableLiveData<Event<UpdateProfileResponse?>>()
    val registrationError: LiveData<Event<UpdateProfileResponse?>>
        get() = _registrationError


    fun registerUser(register: RegisterRequest) {

        setDialogVisibility(true, appInstance.getString(R.string.registering_new_user))
        coroutineScope.launch {

            DataProvider.registerUser(
                request = register,
                success = {


                    _registrationResponse.value = Event(it)
                    if (it.status) {
                        saveRegistrationTokenInSharedPreference(it.data.tmp_token)
                    }


                    setDialogVisibility(false)

                }, error = {
                    checkError(it)
                }
            )
        }


    }


    private fun saveRegistrationTokenInSharedPreference(tempToken: String) {

        HotSpotApp.prefs!!.setRegistrationTempToken(tempToken)

    }


}