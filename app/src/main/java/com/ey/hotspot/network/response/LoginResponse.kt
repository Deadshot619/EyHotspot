package com.ey.hotspot.network.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("token_type") @Expose
    val tokenType: String? = null,
    @SerializedName("expires_in") @Expose
    val expiresIn: Int? = null,
    @SerializedName("access_token") @Expose
    val accessToken: String? = null,
    @SerializedName("refresh_token") @Expose
    val refreshToken: String? = null,

//    @SerializedName("email") @Expose
    val email_error: List<String>? = null,
    @SerializedName("password") @Expose
    val password: List<String>? = null,

    @SerializedName("verification") @Expose
    val verification: Boolean? = null,
    @SerializedName("mobile_no", alternate = ["user_mobile_no"]) @Expose
    val mobile_no: String? = null,
    @SerializedName("email", alternate = ["user_email"]) @Expose
    val email_id: String? = null,
    @SerializedName("tmp_token") @Expose
    val tmpToken: String? = null,
    @SerializedName("is_tc_accepted")@Expose
    val istcaccepted:Boolean?=null

): Parcelable


data class VerificationPending(
    @SerializedName("verification") @Expose
    val verification: Boolean? = null,

    @SerializedName("mobile_no") @Expose
    val mobile_no: String? = null,

    @SerializedName("email") @Expose
    val email_id: String? = null,

    @SerializedName("tmp_token") @Expose
    val tmpToken: String? = null
)