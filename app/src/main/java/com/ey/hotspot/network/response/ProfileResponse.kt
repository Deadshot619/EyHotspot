package com.ey.hotspot.network.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("prefix") val prefix: String,
    @SerializedName("firstname") val firstname: String? = "",
    @SerializedName("firstname_arabic") val firstname_arabic: String,
    @SerializedName("lastname") val lastname: String? = "",
    @SerializedName("lastname_arabic") val lastname_arabic: String,
    @SerializedName("email") val email: String?="",
    @SerializedName("email_verification_key") val email_verification_email_verification_key: String,
    @SerializedName("verified") val verified: Int,
    @SerializedName("email_verified_at") val email_verified_at: String,
    @SerializedName("is_email_verified") val is_email_verified: String,
    @SerializedName("country_code") val country_code: String,
    @SerializedName("mobile_no") val mobile_no: String? = "",
    @SerializedName("otp") val otp: String,
    @SerializedName("is_mobile_verified") val is_mobile_verified: String,
    @SerializedName("mobile_verified_at") val mobile_verified_at: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String,
    @SerializedName("provider") val provider: String? = null,
    @SerializedName("provider_id") val provider_id: String,
    @SerializedName("s_avatar") val s_avatar: String,
    @SerializedName("s_avatar_original") val s_avatar_original: String,
    @SerializedName("s_nickname") val s_nickname: String,
    @SerializedName("s_profileUrl") val s_profileUrl: String,
    @SerializedName("s_sub") val s_sub: String,
    @SerializedName("s_picture") val s_picture: String,
    @SerializedName("s_locale") val s_locale: String,
    @SerializedName("s_link") val s_link: String,
    @SerializedName("language") val language: String,
    @SerializedName("access_token") val access_token: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_by") val created_by: String,
    @SerializedName("updated_by") val updated_by: String,
    @SerializedName("deleted_by") val deleted_by: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("deleted_at") val deleted_at: String
) {
}