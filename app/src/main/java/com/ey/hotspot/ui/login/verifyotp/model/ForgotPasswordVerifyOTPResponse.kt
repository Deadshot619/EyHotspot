package com.ey.hotspot.ui.login.verifyotp.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordVerifyOTPResponse(
    @SerializedName("tmp_token") val tmpToken: String,
    @SerializedName("otp") val otp : List<String>,
    @SerializedName("email") val emailMobile : List<String>

) {
}
