package com.ey.hotspot.ui.speed_test.wifi_log_list

import com.google.gson.annotations.SerializedName

data class WifiLogListRequest(
    @SerializedName("device_id") val deviceId: String
) {
}