package com.ey.hotspot.ui.login.login_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.request.SocialLoginRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.utils.Event
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginFragmentViewModel(application: Application) : BaseViewModel(application) {


    var emailId = ""
    var password = ""

    private val _loginResponse = MutableLiveData<BaseResponse<LoginResponse?>>()
    val loginResponse: LiveData<BaseResponse<LoginResponse?>>
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


                    if (it.status) {
                        _loginResponse.value = it

                        Timber.tag("Bearer_Token").d(it.data?.accessToken)
                        updateSharedPreference(it.data!!)

                    } else {
                        setDialogVisibility(false)
                        _loginError.value = Event(it.data)
                    }


                },
                error = {
                    checkError(it)
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
                    updateSharedPreference(it.data!!)

                    Log.d("TOKEN",it.data.accessToken)

                }, error = {
                    checkError(it)
                    setDialogVisibility(false)
                }
            )
        }

    }


    private fun updateSharedPreference(loginResponse: LoginResponse) {
        HotSpotApp.prefs?.run {
            saveAccessToken(loginResponse.accessToken)
            setAppLoggedInStatus(true)
            setSkipStatus(false)
            setUserDataPref(loginResponse)
        }
    }

    fun setSkippedUserData(){
        HotSpotApp.prefs?.run {
            setAppLoggedInStatus(false)
            setSkipStatus(true)
        }
    }
}