package com.ey.hotspot.ui.speed_test.wifi_log_list

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WifiLogListResponse(

    @SerializedName("id") @Expose val id: Int,
    @SerializedName("user_id") @Expose val user_id: Int,
    @SerializedName("wifi_id") @Expose val wifi_id: Int,
    @SerializedName("device_id") @Expose val device_id: String,
    @SerializedName("average_speed") @Expose val average_speed: Double,
    @SerializedName("login_at") @Expose val login_at: String,
    @SerializedName("logout_at") @Expose val logout_at: String?,
    @SerializedName("created_at") @Expose val created_at: String,
    @SerializedName("updated_at") @Expose val updated_at: String,
    @SerializedName("location") @Expose val location: WifiLogsLocation?
) : Parcelable
