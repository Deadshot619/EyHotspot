package com.ey.hotspot.network.request

import com.google.gson.annotations.SerializedName

data class ForgotPasswordVerifyOTPRequest(
    @SerializedName("otp") val otp: Int,
    @SerializedName("email") val email:String?
) {
}