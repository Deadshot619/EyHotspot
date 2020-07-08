package com.ey.hotspot.ui.login.login_fragment.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
) {
}