package com.ey.hotspot.network.response

import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("tmp_token") val forgotTempToken: String,
    @SerializedName("email") val email: String,
    @SerializedName("email_mobile") val emailMobile : List<String>
) {

}
