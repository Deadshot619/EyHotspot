package com.ey.hotspot.ui.home.models

import com.google.gson.annotations.SerializedName

data class GetHotSpotRequest(
    @SerializedName("lat") val latitude: Double = 0.0,
    @SerializedName("lng") val longitude: Double = 0.0,
    @SerializedName("name") val name: String,
    @SerializedName("locationEnabled") val locationEnabled: Boolean = false

) {
}