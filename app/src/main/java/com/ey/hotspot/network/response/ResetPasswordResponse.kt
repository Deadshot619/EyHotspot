package com.ey.hotspot.network.response

import com.google.gson.annotations.SerializedName

data class ResetPasswordResponse(
    @SerializedName("token") val token : List<String>,
    @SerializedName("email") val email : List<String>,
    @SerializedName("password") val password : List<String>,
    @SerializedName("password_confirmation") val password_confirmation : List<String>
) {


}