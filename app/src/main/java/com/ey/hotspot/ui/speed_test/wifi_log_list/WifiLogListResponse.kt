package com.ey.hotspot.ui.speed_test.wifi_log_list

import com.google.gson.annotations.SerializedName

data class WifiLogListResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("wifi_id") val wifi_id: Int,
    @SerializedName("device_id") val device_id: String,
    @SerializedName("average_speed") val average_speed: Double,
    @SerializedName("login_at") val login_at: String,
    @SerializedName("logout_at") val logout_at: String = "",
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("location") val location: WifiLogsLocation
) {
}