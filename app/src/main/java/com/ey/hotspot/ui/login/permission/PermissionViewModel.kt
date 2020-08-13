package com.ey.hotspot.ui.login.permission

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.TermsRequest
import com.ey.hotspot.network.response.BaseResponse
import kotlinx.coroutines.launch

class PermissionViewModel (application: Application): BaseViewModel(application) {

    private val _termsResponse = MutableLiveData<BaseResponse<Any>>()
    val termsResponse: LiveData<BaseResponse<Any>>
        get() = _termsResponse


    fun callTAndC(termsRequest: TermsRequest)
    {
        setDialogVisibility(true)

        coroutineScope.launch {
            DataProvider.termsAndConditions(
                request = termsRequest,
                success = {
                    _termsResponse.value = it
                   // updateSharedPreference(it.data)
                    setDialogVisibility(false)

                },
                error = {
                    checkError(it)
                    setDialogVisibility(false)
                }
            )
        }
    }
}