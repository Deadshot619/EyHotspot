package com.ey.hotspot.ui.login.logout

import com.ey.hotspot.utils.validations.ErrorDataClass
import com.google.gson.annotations.SerializedName

data class LogoutResponse(@SerializedName("data") val data: ErrorDataClass) {
}