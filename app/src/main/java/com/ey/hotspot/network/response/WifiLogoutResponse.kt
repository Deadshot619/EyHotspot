package com.ey.hotspot.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WifiLogoutResponse(
    @SerializedName("average_speed") @Expose val  average_speed: String,
    @SerializedName("device_id") @Expose val  device_id: String,
    @SerializedName("user_id") @Expose val  user_id: Int,
    @SerializedName("wifi_id") @Expose val  wifi_id: Int
)