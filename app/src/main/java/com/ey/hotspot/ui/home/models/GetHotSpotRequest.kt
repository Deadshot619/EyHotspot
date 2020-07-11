package com.ey.hotspot.ui.home.models

import com.google.gson.annotations.SerializedName

data class GetHotSpotRequest(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double,
    @SerializedName("name") val name: String,
    @SerializedName("locationEnabled") val locationEnabled: Boolean

) {
}