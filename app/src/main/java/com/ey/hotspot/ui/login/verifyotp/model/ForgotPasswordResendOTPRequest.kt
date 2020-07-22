package com.ey.hotspot.ui.login.verifyotp.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResendOTPRequest(

    @SerializedName("email") val email: String?
) {
}