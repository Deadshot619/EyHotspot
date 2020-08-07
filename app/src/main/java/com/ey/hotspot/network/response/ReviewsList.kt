package com.ey.hotspot.network.response

import android.os.Parcelable
import com.ey.hotspot.utils.LANGUAGE
import com.ey.hotspot.utils.constants.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ReviewsList(
    @SerializedName("location_reviews") @Expose val location_reviews: List<LocationReviews>,
    @SerializedName("user_review") @Expose val user_review: LocationReviews? = null
)

@Parcelize
data class LocationReviews(
    @SerializedName("created_at") @Expose val created_at: String,
    @SerializedName("description") @Expose val description: String,
    @SerializedName("diff_for_human") @Expose val diff_for_human: String,
    @SerializedName("firstname") @Expose val _firstname: String,
    @SerializedName("firstname_arabic") @Expose val firstname_arabic: String?,
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("lastname") @Expose val _lastname: String,
    @SerializedName("lastname_arabic") @Expose val lastname_arabic: String?,
    @SerializedName("location") @Expose val _location: String,
    @SerializedName("location_arabic") @Expose val location_arabic: String,
    @SerializedName("location_name") @Expose val _location_name: String,
    @SerializedName("location_name_arabic") @Expose val location_name_arabic: String,
    @SerializedName("provider_name") @Expose val _provider_name: String,
    @SerializedName("provider_name_arabic") @Expose val provider_name_arabic: String,
    @SerializedName("rating") @Expose val rating: Float
): Parcelable {
    val firstname: String
        get() = /*if (LANGUAGE == Constants.ARABIC_LANG) firstname_arabic ?: _firstname else*/ _firstname

    val lastname: String
        get() = /*if (LANGUAGE == Constants.ARABIC_LANG) lastname_arabic ?: _lastname else*/ _lastname

    val location: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) location_arabic else _location

    val location_name: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) location_name_arabic else _location_name

    val provider_name: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) provider_name_arabic else _provider_name
}