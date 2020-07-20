package com.ey.hotspot.ui.home.models

import com.google.gson.annotations.SerializedName

data class GetHotSpotResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("name_arabic") val name_arabic: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
    @SerializedName("location") val location: String,
    @SerializedName("location_arabic") val location_arabic: String,
    @SerializedName("type") val type: String,
    @SerializedName("type_arabic") val type_arabic: String,
    @SerializedName("average_rating") val average_rating: String,
    @SerializedName("provider_name") val provider_name: String,
    @SerializedName("provider_name_arabic") val provider_name_arabic: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("navigate_url") val navigate_url: String,
    @SerializedName("favourite") val favourite : Boolean = false
) {
}