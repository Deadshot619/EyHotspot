package com.ey.hotspot.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MatchWifiRequest(
    @SerializedName("wifi_name") @Expose val wifi_name: String
)