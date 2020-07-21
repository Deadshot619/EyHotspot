package com.ey.hotspot.ui.login.verifyotp.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordVerifyOTPRequest(
    @SerializedName("otp") val otp: Int
) {
}