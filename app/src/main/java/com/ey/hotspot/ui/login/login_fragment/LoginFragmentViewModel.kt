package com.ey.hotspot.ui.login.login_fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.ui.login.login_fragment.model.LoginRequest
import com.ey.hotspot.ui.login.login_fragment.model.LoginResponse
import com.google.gson.JsonArray
import kotlinx.coroutines.launch

class LoginFragmentViewModel(application: Application) : BaseViewModel(application) {


    var emailId = ""
    var password = ""

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse


    //Call this method from fragment/layout
    fun callLogin(loginRequest: LoginRequest) {

        coroutineScope.launch {
            DataProvider.login(
                request = loginRequest,
                success = {
                    _loginResponse.value = it
                },
                error = {
                    _errorText.value = it.message
                }
            )
        }
    }
}