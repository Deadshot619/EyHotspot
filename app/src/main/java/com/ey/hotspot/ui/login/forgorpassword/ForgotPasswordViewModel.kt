package com.ey.hotspot.ui.login.forgorpassword

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.ForgotPasswordRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.ForgotPasswordResponse
import com.ey.hotspot.utils.Event
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(application: Application) : BaseViewModel(application) {

    var mEmailIdOrPassword: String = ""


    private val _forgotPasswordResponse = MutableLiveData<Event<BaseResponse<ForgotPasswordResponse>>>()
    val forgotPasswordResponse: LiveData<Event<BaseResponse<ForgotPasswordResponse>>>
        get() = _forgotPasswordResponse


    fun callForgotPasswordAPI(forgotPasswordRequest: ForgotPasswordRequest) {

        setDialogVisibility(true)

        coroutineScope.launch {

            DataProvider.forgotPassword(
                request = forgotPasswordRequest,
                success = {
                    _forgotPasswordResponse.value = Event(it)
                    if (it.status) {
                        saveForgotPassswordTokenInSharedPreference(it.data.forgotTempToken)
                        saveForgotPasswordEmail(it.data.email)
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


    private fun saveForgotPasswordEmail(email: String) {

        HotSpotApp.prefs!!.setForgotPasswordField(email)

    }
}