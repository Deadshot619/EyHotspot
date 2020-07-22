package com.ey.hotspot.ui.login.changepassword

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.login.changepassword.model.ResetPasswordRequest
import com.ey.hotspot.ui.login.changepassword.model.ResetPasswordResponse
import kotlinx.coroutines.launch

class ChangePasswordViewModel(application: Application) : BaseViewModel(application) {


    var emailId: String? = ""
    var password: String = ""
    var confirmPassword: String = ""

    private val _resetPasswordResponse = MutableLiveData<BaseResponse<ResetPasswordResponse>>()
    val resetPassworResponse: LiveData<BaseResponse<ResetPasswordResponse>>
        get() = _resetPasswordResponse


    fun callResetPassworAPI(resetPasswordRequest: ResetPasswordRequest) {

        setDialogVisibility(true)

        coroutineScope.launch {

            DataProvider.resetPassword(
                request = resetPasswordRequest,
                success = {
                    _resetPasswordResponse.value = it
                    setDialogVisibility(false)
                }, error = {

                    checkError(it)
                }
            )
        }
    }
}