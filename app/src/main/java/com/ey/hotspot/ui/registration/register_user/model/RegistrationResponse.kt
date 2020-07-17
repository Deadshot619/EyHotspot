package com.ey.hotspot.ui.registration.register_user.model

import com.google.gson.annotations.SerializedName

data class RegistrationResponse(

    @SerializedName("tmp_token") val tmp_token : String,
    @SerializedName("first_name") val first_name : List<String>,
    @SerializedName("last_name") val last_name : List<String>,
    @SerializedName("country_code") val country_code : List<String>,
    @SerializedName("mobile_no") val mobile_no : List<String>,
    @SerializedName("email") val email : List<String>,
    @SerializedName("password") val password : List<String>
)
{
}