package com.ey.hotspot.network.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordVerifyOTPResponse(
    @SerializedName("tmp_token") val tmpToken: String,
    @SerializedName("otp") val otp : List<String>,
    @SerializedName("email") val emailMobile : List<String>

) {
}
