package com.ey.hotspot.ui.login.otpverification.fragment.model

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(@SerializedName("otp") val otp: Int) {
}