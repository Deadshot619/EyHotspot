package com.ey.hotspot.ui.login.logout

import com.ey.hotspot.ui.registration.register_user.model.ErrorDataClass
import com.google.gson.annotations.SerializedName

data class LogoutResponse(@SerializedName("data") val data: ErrorDataClass) {
}