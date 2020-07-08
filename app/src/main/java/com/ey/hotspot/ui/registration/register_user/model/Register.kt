package com.ey.hotspot.ui.registration.register_user.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Register(

    @SerializedName("first_name") @Expose
    val firstName: String,

    @SerializedName("last_name") @Expose
    val lastName: String,

    @SerializedName("country_code") @Expose
    val countryCode: String,

    @SerializedName("mobile_no") @Expose
    val mobileNo: String,

    @SerializedName("email") @Expose
    val email: String,

    @SerializedName("password") @Expose
    val password: String,

    @SerializedName("confirm_password") @Expose
    val confirmPassword: String
)