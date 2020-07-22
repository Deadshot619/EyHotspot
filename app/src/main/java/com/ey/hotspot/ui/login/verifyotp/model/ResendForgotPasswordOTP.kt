package com.ey.hotspot.ui.login.verifyotp.model

import com.google.gson.annotations.SerializedName

data class ResendForgotPasswordOTP(

  @SerializedName("otp") val otp:String,
  @SerializedName("email") val email: List<String> = listOf()
 ) {
}