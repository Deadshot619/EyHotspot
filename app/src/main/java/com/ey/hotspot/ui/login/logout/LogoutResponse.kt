package com.ey.hotspot.ui.login.logout

import com.google.gson.annotations.SerializedName

data class LogoutResponse(@SerializedName("message") val message: String) {
}