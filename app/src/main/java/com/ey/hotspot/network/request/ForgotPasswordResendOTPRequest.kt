package com.ey.hotspot.network.request

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResendOTPRequest(

    @SerializedName("email") val email: String?
) {
}