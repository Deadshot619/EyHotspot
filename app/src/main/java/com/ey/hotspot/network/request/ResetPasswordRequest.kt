package com.ey.hotspot.network.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("email") val email:String?,
    @SerializedName("password") val password:String,
    @SerializedName("password_confirmation") val confirmPassword:String,
    @SerializedName("token") val token:String?,
    @SerializedName("otp") val otp:String?


) {
}