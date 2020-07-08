package com.ey.hotspot.ui.registration.register_user.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expireIn: Int,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String

) {
}