package com.ey.hotspot.ui.registration.register_user.model

import com.google.gson.annotations.SerializedName

data class ErrorDataClass(
    @SerializedName("tmp_token") val mobile_no: String

) {
}