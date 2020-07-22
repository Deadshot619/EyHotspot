package com.ey.hotspot.ui.registration.register_user.model

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    /*
    Success Response
    * */

    @SerializedName("tmp_token") val tmp_token: String = "",

    /*Failure Response*/
    @SerializedName("first_name") val first_name: List<String> = listOf(),
    @SerializedName("last_name") val last_name: List<String> = listOf(),
    @SerializedName("country_code") val country_code: List<String> = listOf(),
    @SerializedName("mobile_no") val mobile_no: List<String> = listOf(),
    @SerializedName("email") val email: List<String> = listOf(),
    @SerializedName("password") val password: List<String> = listOf()
) {
}