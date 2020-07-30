package com.ey.hotspot.network.request

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(@SerializedName("otp") val otp: Int) {
}