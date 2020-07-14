package com.ey.hotspot.network.request

import com.google.gson.annotations.SerializedName

data class SocialLoginRequest(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("provider")
    val provider: String,
    @SerializedName("provider_id")
    val providerId: String,
    @SerializedName("token")
    val token: String
) {
}