package com.ey.hotspot.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    /*
    Success Response
    * */

    @SerializedName("tmp_token") val tmp_token: String = "",

    /*Failure Response*/
    @SerializedName("first_name") @Expose val firstName: List<String>? = null,
    @SerializedName("last_name")  @Expose val lastName: List<String>? = null,
    @SerializedName("mobile_no")  @Expose val mobileNo: List<String>? = null,
    @SerializedName("country_code")  @Expose val countryCode: List<String>? = null,
    @SerializedName("email")  @Expose val email: List<String>? = null,
    @SerializedName("password")  @Expose val password: List<String>? = null,
    @SerializedName("confirm_password")  @Expose val confirmPassword: List<String>? = null

) {
}