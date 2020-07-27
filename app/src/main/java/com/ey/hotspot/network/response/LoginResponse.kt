package com.ey.hotspot.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token_type") @Expose
    val tokenType: String,
    @SerializedName("expires_in") @Expose
    val expiresIn: Int,
    @SerializedName("access_token") @Expose
    val accessToken: String,
    @SerializedName("refresh_token") @Expose
    val refreshToken: String,

    @SerializedName("email") @Expose
    val email: List<String>? = null,
    @SerializedName("password") @Expose
    val password: List<String>? = null,

    @SerializedName("verification") @Expose
    val verification: Boolean?=null,

    @SerializedName("mobile_no") @Expose
    val mobile_no: String?=null,

    @SerializedName("email_id") @Expose
    val email_id: String?=null,

    @SerializedName("tmp_token") @Expose
    val tmpToken: String?=null


) {
}



data class VerificationPending(
    @SerializedName("verification") @Expose
    val verification: Boolean?=null,

    @SerializedName("mobile_no") @Expose
    val mobile_no: String?=null,

    @SerializedName("email_id") @Expose
    val email_id: String?=null,

    @SerializedName("tmp_token") @Expose
    val tmpToken: String?=null
)