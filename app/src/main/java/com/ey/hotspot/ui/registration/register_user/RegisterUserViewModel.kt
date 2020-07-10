package com.ey.hotspot.ui.registration.register_user

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.ui.registration.register_user.model.RegistrationResponse
import kotlinx.coroutines.launch

class RegisterUserViewModel(application: Application) : BaseViewModel(application) {

    var firstName = ""
    var lastName = ""
    var emailId = ""
    var password = ""
    var confirmPassword = ""
    var mobileNumber = ""
    var coutrycode = ""


    private val _registrationResponse = MutableLiveData<RegistrationResponse>()

    val registrationResponse: LiveData<RegistrationResponse>
        get() = _registrationResponse


    fun registerUser(register: RegisterRequest) {

        coroutineScope.launch {

            DataProvider.registerUser(
                request = register,
                success = {


                    _errorText.value = it.message


                }, error = {
                    Log.d(
                        "ErrorResponse", it.message
                    )
                    _errorText.value = it.message
                }
            )
        }


    }




}