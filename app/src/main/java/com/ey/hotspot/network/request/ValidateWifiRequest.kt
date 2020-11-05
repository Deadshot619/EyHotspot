package com.ey.hotspot.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateWifiRequest(
    @SerializedName("wifi_name") @Expose val  wifi_name: String,
    @SerializedName("lat") @Expose val lat: Double,
    @SerializedName("lng") @Expose val lng: Double
)