package com.ey.hotspot.ui.login.otpverification.fragment.model

import com.google.gson.annotations.SerializedName

data class SendOTPRequest(@SerializedName("selected_option") val selected_option:String) {
}