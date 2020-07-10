package com.ey.hotspot.ui.registration.register_user.model

import com.ey.hotspot.utils.validations.ErrorDataClass
import com.google.gson.annotations.SerializedName

data class RegistrationResponse(

    @SerializedName("data") val data: ErrorDataClass
) {
}