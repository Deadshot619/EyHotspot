package com.ey.hotspot.network.request

import com.google.gson.annotations.SerializedName

data class WifiLogListRequest(
    @SerializedName("device_id") val deviceId: String
) {
}