package com.ey.hotspot.ui.login.otpverification.fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.login.otpverification.fragment.model.SendOTPRequest
import com.ey.hotspot.ui.login.otpverification.fragment.model.VerifyOTPRequest
import com.ey.hotspot.utils.constants.updateSharedPreference
import kotlinx.coroutines.launch

class OTPVerificationViewModel(application: Application) : BaseViewModel(application) {


    private val _sendOTPResponse = MutableLiveData<BaseResponse<Any>>()
    val sendOTPResponse: LiveData<BaseResponse<Any>>
        get() = _sendOTPResponse


    private val _verifyOTPResponse = MutableLiveData<BaseResponse<LoginResponse>>()
    val verifyResponse: LiveData<BaseResponse<LoginResponse>>
        get() = _verifyOTPResponse

    fun callSendOTPRequest(sendOTPRequest: SendOTPRequest) {
        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.sendOTP(sendOTPRequest,

                success = {
                    setDialogVisibility(false)
                    _sendOTPResponse.value = it
                    Log.d("Verify", it.message)

                }
                , error = {
                    checkError(it)
                    setDialogVisibility(false)
                })
        }

    }

    fun verfiyOTPRequest(verifyOTPRequest: VerifyOTPRequest) {
        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.verifyOTP(verifyOTPRequest,
                success = {
                    setDialogVisibility(false)

                    _verifyOTPResponse.value = it

                    updateSharedPreference(it.data)
                    Log.d("Verify", it.message)
                },
                error = {
                    checkError(it)
                })

        }
    }
}