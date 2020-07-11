package com.ey.hotspot.ui.login.login_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.login.logout.LogoutResponse
import com.ey.hotspot.ui.login.logout.RefreshToken
import kotlinx.coroutines.launch

class LoginFragmentViewModel(application: Application) : BaseViewModel(application) {


    var emailId = ""
    var password = ""

    private val _loginResponse = MutableLiveData<BaseResponse<LoginResponse>>()
    val loginResponse: LiveData<BaseResponse<LoginResponse>>
        get() = _loginResponse


    private val _logoutResponse = MutableLiveData<BaseResponse<LogoutResponse>>()
    val logoutResponse: LiveData<BaseResponse<LogoutResponse>>
        get() = _logoutResponse


    private val _refreshTokenResponse = MutableLiveData<BaseResponse<RefreshToken>>()
    val refreshTokenResponse: LiveData<BaseResponse<RefreshToken>>
        get() = _refreshTokenResponse


    //Call this method from fragment/layout
    fun callLogin(loginRequest: LoginRequest) {
        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.login(
                request = loginRequest,
                success = {

                    if (it.status) {
                        _loginResponse.value = it

                        Log.d("LoginSuccess", it.data.accessToken)
                        _errorText.value = it.message

                        updateSharedPreference(it.data)

                        Log.d(
                            "GetAccessToken", HotSpotApp.prefs!!.getAccessToken()
                        )
                    }

                    setDialogVisibility(false)
                },
                error = {
                    checkError(it)
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
            DataProvider.refreshToken(
                success = {
                    _refreshTokenResponse.value = it
                }, error = {
                    checkError(it)
                }
            )
        }
    }


    private fun updateSharedPreference(loginResponse: LoginResponse) {

        HotSpotApp.prefs?.saveAccessToken(loginResponse.accessToken)
        HotSpotApp.prefs?.setAppLoggedInStatus(true)

    }
}