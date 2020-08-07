package com.ey.hotspot.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(

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
    val confirmPassword: String,

    @SerializedName("is_tc_accepted") @Expose
    val isTcAccepted: Boolean

)