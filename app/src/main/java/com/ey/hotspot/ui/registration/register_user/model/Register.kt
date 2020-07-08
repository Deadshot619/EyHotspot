package com.ey.hotspot.ui.registration.register_user.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class Register(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("mobile_no")
    val mobileNo: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("confirm_password")
    val confirmPassword: String

) {
}