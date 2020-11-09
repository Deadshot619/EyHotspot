package com.ey.hotspot.network.response

import android.os.Parcelable
import com.ey.hotspot.utils.LANGUAGE
import com.ey.hotspot.utils.constants.Constants
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
    @SerializedName("speed_test") @Expose val speed_test:  List<WifispeedtestData>? = null,
    @SerializedName("location") @Expose val location: WifiLogsLocation?
) : Parcelable

@Parcelize
class WifispeedtestData(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("user_id") @Expose val user_id: Int,
    @SerializedName("wifi_id") @Expose val wifi_id: Int,
    @SerializedName("wifi_user_id") @Expose val wifi_user_id: Int,
    @SerializedName("device_id") @Expose val device_id: String,
    @SerializedName("average_speed") @Expose val average_speed: Double,
    @SerializedName("created_at") @Expose val created_at: String,
    @SerializedName("updated_at") @Expose val updated_at: String
) : Parcelable
