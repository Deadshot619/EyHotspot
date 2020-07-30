package com.ey.hotspot.network.request

import com.google.gson.annotations.SerializedName

data class ForgotPasswordRequest(
    @SerializedName("email_mobile") val emaiMobile: String
) {
}