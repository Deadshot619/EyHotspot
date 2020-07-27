package com.ey.hotspot.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WifiLoginResponse(
    @SerializedName("average_speed") @Expose val  average_speed: String,
    @SerializedName("created_at") @Expose val  created_at: String,
    @SerializedName("device_id") @Expose val  device_id: String,
    @SerializedName("id") @Expose val  id: Int,
    @SerializedName("login_at") @Expose val  login_at: String,
    @SerializedName("updated_at") @Expose val updated_at: String,
    @SerializedName("user_id") @Expose val user_id: String,
    @SerializedName("wifi_id") @Expose val wifi_id: String
)