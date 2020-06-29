package com.ey.hotspot.ui.registration.register_user

import android.app.Application
import com.ey.hotspot.app_core_lib.BaseViewModel

class RegisterUserViewModel(application: Application) : BaseViewModel(application) {

    var firstName = ""
    var lastName = ""
    var emailId = ""
    var password = ""
    var confirmPassword = ""
    var mobileNumber = ""

}