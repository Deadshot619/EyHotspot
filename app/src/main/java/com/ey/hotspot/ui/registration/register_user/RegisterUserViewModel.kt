package com.ey.hotspot.ui.registration.register_user

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterUserViewModel(application: Application) : BaseViewModel(application) {

    var firstName = ""
    var lastName = ""
    var emailId = ""
    var password = ""
    var confirmPassword = ""
    var mobileNumber = ""
    var coutrycode = ""


    private val _registrationResponse = MutableLiveData<RegisterResponse>()

    val registrationResponse: LiveData<RegisterResponse>
        get() = _registrationResponse


    fun registerUser(register: RegisterRequest) {

        coroutineScope.launch {

            DataProvider.registerUser(
                request = register,
                success = {
                    _registrationResponse.value = it
                    Log.d("SuccessReponse", it.accessToken)
                }, error = {
                    Log.d(
                        "ErrorResponse", it.message
                    )
                }
            )
        }
    }

}