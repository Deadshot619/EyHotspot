package com.ey.hotspot.ui.login.verifyotp.model

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(
    @SerializedName("email_mobile") val emaiMobile: String
) {
}