package com.ey.hotspot.ui.login.login_fragment

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.request.SocialLoginRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.utils.Event
import com.ey.hotspot.utils.extention_functions.showMessage
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginFragmentViewModel(application: Application) : BaseViewModel(application) {


    var emailId = ""
    var password = ""
    var captcha = ""

    private val _loginResponse = MutableLiveData<Event<BaseResponse<LoginResponse?>>>()
    val loginResponse: LiveData<Event<BaseResponse<LoginResponse?>>>
        get() = _loginResponse

    //This variable will handle Login Errors
    private val _loginError = MutableLiveData<Event<LoginResponse?>>()
    val loginError: LiveData<Event<LoginResponse?>>
        get() = _loginError


    private val _socialLoginRespinse = MutableLiveData<BaseResponse<LoginResponse?>>()
    val socialLoginResponse: LiveData<BaseResponse<LoginResponse?>>
        get() = _socialLoginRespinse



    //Call this method from fragment/layout
    fun callLogin(loginRequest: LoginRequest) {
        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.login(
                request = loginRequest,
                success = {
                    setDialogVisibility(false)
                    _loginResponse.value = Event(it)
                },
                error = {
                    checkError(it)
                    setDialogVisibility(false)
                }
            )
        }
    }

    fun callSocialLogin(socialLoginRequest: SocialLoginRequest) {
        setDialogVisibility(true)
        coroutineScope.launch {

            DataProvider.socialLogin(
                request = socialLoginRequest,
                success = {
                    setDialogVisibility(false)
                    _socialLoginRespinse.value = it

//                    updateSharedPreference(it.data!!)

                }, error = {
                    checkError(it)
                    setDialogVisibility(false)
                }
            )
        }

    }
}