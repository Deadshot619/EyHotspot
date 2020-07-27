package com.ey.hotspot.utils.extention_functions

import android.app.Activity
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.network.response.VerificationPending


/*
This function maps login response model to verification pending model
* */
fun LoginResponse.toVerificationPending() = VerificationPending(
    verification = this.verification,
    mobile_no = this.mobile_no,
    email_id = this.email_id,
    tmpToken = this.tmpToken
)