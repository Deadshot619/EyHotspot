package com.ey.hotspot.ui.home.models

import com.ey.hotspot.utils.LANGUAGE
import com.ey.hotspot.utils.constants.Constants
import com.google.gson.annotations.SerializedName

data class GetHotSpotResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val _name: String,
    @SerializedName("name_arabic") val name_arabic: String,
    @SerializedName("lat") val lat: String,
    @SerializedName("lng") val lng: String,
    @SerializedName("location") val _location: String,
    @SerializedName("location_arabic") val location_arabic: String,
    @SerializedName("type") val _type: String?,
    @SerializedName("type_arabic") val type_arabic: String?,
    @SerializedName("average_rating") val average_rating: String?,
    @SerializedName("provider_name") val _provider_name: String,
    @SerializedName("provider_name_arabic") val provider_name_arabic: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("navigate_url") val navigate_url: String,
    @SerializedName("favourite") var favourite: Boolean = false
) {
    val name: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) name_arabic else _name

    val location: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) location_arabic else _location

    val type: String?
        get() = if (LANGUAGE == Constants.ARABIC_LANG) type_arabic else _type

    val provider_name: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) provider_name_arabic else _provider_name
}