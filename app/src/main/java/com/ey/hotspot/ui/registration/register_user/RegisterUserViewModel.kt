package com.ey.hotspot.ui.registration.register_user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.BaseResponse
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


    private val _registrationResponse = MutableLiveData<BaseResponse<RegistrationResponse>>()

    val registrationResponse: LiveData<BaseResponse<RegistrationResponse>>
        get() = _registrationResponse


    fun registerUser(register: RegisterRequest) {


        setDialogVisibility(true, appInstance.getString(R.string.registering_new_user))
        coroutineScope.launch {

            DataProvider.registerUser(
                request = register,
                success = {


                    showToastFromViewModel(it.message)

                    setDialogVisibility(false)

                }, error = {


                    checkError(it)
                }
            )
        }


    }




}