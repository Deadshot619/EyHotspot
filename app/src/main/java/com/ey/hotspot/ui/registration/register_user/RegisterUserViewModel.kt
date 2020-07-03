package com.ey.hotspot.ui.registration.register_user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.google.gson.JsonArray
import kotlinx.coroutines.launch

class RegisterUserViewModel(application: Application) : BaseViewModel(application) {

    var firstName = ""
    var lastName = ""
    var emailId = ""
    var password = ""
    var confirmPassword = ""
    var mobileNumber = ""


    private val _registrationResponse = MutableLiveData<JsonArray>()

    val registrationResponse :LiveData<JsonArray>
    get() = _registrationResponse


    fun registerUser(name:String,mobileNo:String,emailId:String, password:String,confirmPassword:String){

        coroutineScope.launch {
            DataProvider.registration(
                success = {
                    _registrationResponse
                },error = {

                }
            )
        }
    }

}