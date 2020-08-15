package com.ey.hotspot.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SpeedTestRequest (
    @SerializedName("average_speed") @Expose val average_speed: Double,
    @SerializedName("device_id") @Expose val device_id: String,
    @SerializedName("wifi_id") @Expose val wifi_id: Int,
    @SerializedName("mode") @Expose val mode: String
)