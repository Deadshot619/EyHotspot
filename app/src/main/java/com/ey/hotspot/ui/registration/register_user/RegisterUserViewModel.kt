package com.ey.hotspot.ui.registration.register_user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.CoutryCode
import com.ey.hotspot.network.response.RegistrationResponse
import com.ey.hotspot.network.response.UpdateProfileResponse
import com.ey.hotspot.utils.Event
import com.ey.hotspot.utils.constants.Constants
import kotlinx.coroutines.launch

class RegisterUserViewModel(application: Application) : BaseViewModel(application) {

    var firstName = ""
    var lastName = ""
    var emailId = ""
    var password = ""
    var confirmPassword = ""
    var mobileNumber = ""
    var coutrycode = Constants.SAUDI_ARABIA_COUNTRY_CODE.toString()


    private val _registrationResponse = MutableLiveData<Event<BaseResponse<RegistrationResponse>>>()
    val registrationResponse: LiveData<Event<BaseResponse<RegistrationResponse>>>
        get() = _registrationResponse

    //This variable will handle Profile Errors
    private val _registrationError = MutableLiveData<Event<UpdateProfileResponse?>>()
    val registrationError: LiveData<Event<UpdateProfileResponse?>>
        get() = _registrationError

    //Country Codes
    private val _getCoutryCodeList = MutableLiveData<Event<CoutryCode>>()
    val getCountryCodeList: LiveData<Event<CoutryCode>>
        get() = _getCoutryCodeList


    init {
        getCountryCodeList()
    }


    fun registerUser(register: RegisterRequest) {
        setDialogVisibility(true, appInstance.getString(R.string.registering_new_user))
        coroutineScope.launch {

            DataProvider.registerUser(
                request = register,
                success = {
                    _registrationResponse.value = Event(it)
                    if (it.status) {
                        saveRegistrationTokenInSharedPreference(it.data.tmp_token)
                    }


                    setDialogVisibility(false)

                }, error = {
                    checkError(it)
                }
            )
        }
    }

    //Method to get Country code list
    private fun getCountryCodeList() {
        setDialogVisibility(true)
        coroutineScope.launch {

            DataProvider.getCountryCode(
                success = {
                    if (it.status)
                        _getCoutryCodeList.value = Event(it.data)
                    else
                        showToastFromViewModel(it.message)

                    setDialogVisibility(false)
                },
                error = {
                    checkError(it)
                }
            )
        }
    }

    private fun saveRegistrationTokenInSharedPreference(tempToken: String) {
        HotSpotApp.prefs!!.setRegistrationTempToken(tempToken)
    }
}