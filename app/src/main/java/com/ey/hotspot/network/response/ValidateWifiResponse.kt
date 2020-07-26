package com.ey.hotspot.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateWifiResponse(
    @SerializedName("average_speed") @Expose val  average_speed: Double? = 0.0,
    @SerializedName("distance") @Expose val  distance: Double = 0.0,
    @SerializedName("id") @Expose val  id: Int = 0,
    @SerializedName("number_of_users_connected") @Expose val  number_of_users_connected: Int = 0,
    @SerializedName("wifi_name") @Expose val  wifi_name: String = "-"
)