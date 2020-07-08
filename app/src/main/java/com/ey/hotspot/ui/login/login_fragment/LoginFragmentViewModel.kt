package com.ey.hotspot.ui.login.login_fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.google.gson.JsonArray
import kotlinx.coroutines.launch

class LoginFragmentViewModel(application: Application): BaseViewModel(application)  {


    var emailId=""
    var password=""

    private val _loginResponse = MutableLiveData<JsonArray>()
    val loginResponse: LiveData<JsonArray>
        get() = _loginResponse


    //Call this method from fragment/layout
    fun callLogin(email: String, password: String){
        //request model, if exists
/*        val loginRequestModel = LoginRequestModel(email, password)*/

        coroutineScope.launch {
            DataProvider.login(
                /*request = loginRequestModel,*/
                success = {
                    _loginResponse.value = it
                },
                error = {
                    //Whatever msg we have to display to user
                }
            )
        }
    }
}