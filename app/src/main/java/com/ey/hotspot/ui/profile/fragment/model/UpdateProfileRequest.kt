package com.ey.hotspot.ui.profile.fragment.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("mobile_no") val mobileNo: String,
    @SerializedName("country_code") val countryCode: String,
    @SerializedName("email") val email: String/*,
    @SerializedName("password") val password: String,
    @SerializedName("confirm_password") val confirmPassword: String*/
) {
}