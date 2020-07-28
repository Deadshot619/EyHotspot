package com.ey.hotspot.ui.speed_test.wifi_log_list

import com.google.gson.annotations.SerializedName

data class WifiLogsLocation(
    @SerializedName("id") val id: Int,
    @SerializedName("wifi_name") val wifi_name: String
) {
}