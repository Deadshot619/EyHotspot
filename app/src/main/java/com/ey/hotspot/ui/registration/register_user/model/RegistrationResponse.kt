package com.ey.hotspot.ui.registration.register_user.model

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(

    @SerializedName("tmp_token") val tempToken: String
) {
}