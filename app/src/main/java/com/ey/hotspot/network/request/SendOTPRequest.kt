package com.ey.hotspot.network.request

import com.google.gson.annotations.SerializedName

data class SendOTPRequest(@SerializedName("selected_option") val selected_option:String) {
}