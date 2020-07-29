package com.ey.hotspot.ui.speed_test.wifi_log_list

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.DecimalFormat

@Parcelize
data class WifiLogsLocation(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("name") @Expose val name: String?,
    @SerializedName("uuid") @Expose val uuid: String,
    @SerializedName("name_arabic") @Expose val name_arabic: String,
    @SerializedName("lat") @Expose val lat: String,
    @SerializedName("lng") @Expose val lng: String,
    @SerializedName("location") @Expose val location: String,
    @SerializedName("location_arabic") @Expose val location_arabic: String,
//    @SerializedName("type")val type:String,
    //  @SerializedName("type_arabic") val type_arabic:String,
    @SerializedName("average_rating") @Expose val average_rating: Double,
    @SerializedName("provider_name") @Expose val provider_name: String,
    @SerializedName("provider_name_arabic") @Expose val provider_name_arabic: String

) : Parcelable