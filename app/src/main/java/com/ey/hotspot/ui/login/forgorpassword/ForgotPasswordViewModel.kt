package com.ey.hotspot.ui.login.forgorpassword

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordResponse
import kotlinx.coroutines.launch

class ForgotPasswordViewModel (application: Application): BaseViewModel(application) {

    var mEmailIdOrPassword:String=""


    private val _forgotPasswordResponse = MutableLiveData<BaseResponse<ForgotPasswordResponse>>()
    val forgotPasswordResponse: LiveData<BaseResponse<ForgotPasswordResponse>>
        get() = _forgotPasswordResponse


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


    private fun saveForgotPassswordTokenInSharedPreference(tempToken: String) {

        HotSpotApp.prefs!!.setRegistrationTempToken(tempToken)
        Log.d("ForgotPasswordTOken", tempToken)
    }

}