package com.ey.hotspot.ui.login.verifyotp

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.login.verifyotp.model.*
import kotlinx.coroutines.launch

class VerifyOTPViewModel(application: Application) : BaseViewModel(application) {


    private val _forgotPasswordVerifyOTPResponse =
        MutableLiveData<BaseResponse<ForgotPasswordVerifyOTPResponse>>()
    val forgotPasswordVerifyOTPResponse: LiveData<BaseResponse<ForgotPasswordVerifyOTPResponse>>
        get() = _forgotPasswordVerifyOTPResponse


    private val _resendOTPResponse = MutableLiveData<BaseResponse<ResendForgotPasswordOTP>>()
    val resendOTPResponse: LiveData<BaseResponse<ResendForgotPasswordOTP>>
        get() = _resendOTPResponse


    fun verifyForgotPasswordOTP(forgotPasswordVerifyOTPRequest: ForgotPasswordVerifyOTPRequest) {
        setDialogVisibility(true)

        coroutineScope.launch {

            DataProvider.forgotPasswordVerifyOTP(
                request = forgotPasswordVerifyOTPRequest,
                success = {

                    _forgotPasswordVerifyOTPResponse.value = it
                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }
            )
        }

    }


    fun resendForgotPasswordOTP(forgotPasswordResendOTPRequest: ForgotPasswordResendOTPRequest) {

        setDialogVisibility(true)
        coroutineScope.launch {

            DataProvider.resendForgotPasswordOTP(
                request = forgotPasswordResendOTPRequest,
                success = {

                    setDialogVisibility(false)
                    _resendOTPResponse.value = it
                }, error = {
                    checkError(it)
                }
            )
        }
    }


}