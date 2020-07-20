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
import com.ey.hotspot.ui.login.logout.LogoutResponse
import kotlinx.coroutines.launch
import timber.log.Timber

class LoginFragmentViewModel(application: Application) : BaseViewModel(application) {


    var emailId = ""
    var password = ""

    private val _loginResponse = MutableLiveData<BaseResponse<LoginResponse?>>()
    val loginResponse: LiveData<BaseResponse<LoginResponse?>>
        get() = _loginResponse


    private val _logoutResponse = MutableLiveData<BaseResponse<LogoutResponse>>()
    val logoutResponse: LiveData<BaseResponse<LogoutResponse>>
        get() = _logoutResponse


    private val _refreshTokenResponse = MutableLiveData<BaseResponse<LoginResponse>>()
    val refreshTokenResponse: LiveData<BaseResponse<LoginResponse>>
        get() = _refreshTokenResponse


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


    fun logOut() {
        coroutineScope.launch {

            DataProvider.logout(
                success = {
                    _logoutResponse.value = it
                },
                error = {
                    checkError(it)
                }
            )
        }
    }

    fun refreshToken() {

        coroutineScope.launch {
            DataProvider.refreshTokenAsync(
                success = {
                    _refreshTokenResponse.value = it
                }, error = {
                    checkError(it)
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