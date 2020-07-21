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
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordResponse
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordVerifyOTPRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordVerifyOTPResponse
import kotlinx.coroutines.launch

class VerifyOTPViewModel(application: Application) : BaseViewModel(application) {

    private val _forgotPasswordResponse = MutableLiveData<BaseResponse<ForgotPasswordResponse>>()
    val forgotPasswordResponse: LiveData<BaseResponse<ForgotPasswordResponse>>
        get() = _forgotPasswordResponse


    private val _forgotPasswordVerifyOTPResponse =
        MutableLiveData<BaseResponse<ForgotPasswordVerifyOTPResponse>>()
    val forgotPasswordVerifyOTPResponse: LiveData<BaseResponse<ForgotPasswordVerifyOTPResponse>>
        get() = _forgotPasswordVerifyOTPResponse

    fun callForgotPasswordAPI(forgotPasswordRequest: ForgotPasswordRequest) {

        setDialogVisibility(true)

        coroutineScope.launch {

            DataProvider.forgotPassword(
                request = forgotPasswordRequest,
                success = {
                    _forgotPasswordResponse.value = it
                    if (it.status == true) {
                        saveForgotPassswordTokenInSharedPreference(it.data.forgotTempToken)
                    }
                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }
            )
        }

    }


    fun verifyForgotPasswordOTP(forgotPasswordVerifyOTPRequest: ForgotPasswordVerifyOTPRequest) {
        setDialogVisibility(true)

        coroutineScope.launch {

            DataProvider.forgotPasswordVerifyOTP(
                request = forgotPasswordVerifyOTPRequest,
                success = {

                    _forgotPasswordVerifyOTPResponse.value = it
                    if (it.status == true) {
                        saveVerifyForgotPassswordTokenInSharedPreference(it.data.tmpToken)
                    }
                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }
            )
        }

    }


    private fun saveForgotPassswordTokenInSharedPreference(tempToken: String) {

        HotSpotApp.prefs!!.setRegistrationTempToken(tempToken)
        Log.d("ForgotPasswordTOken", tempToken)
    }


    private fun saveVerifyForgotPassswordTokenInSharedPreference(tempToken: String) {

        HotSpotApp.prefs!!.setVerifyForgotPasswordToken(tempToken)
        Log.d("VerifyForgotPassword", tempToken)
    }

}