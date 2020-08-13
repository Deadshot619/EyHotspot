package com.ey.hotspot.network.response

import android.os.Parcelable
import com.ey.hotspot.utils.LANGUAGE
import com.ey.hotspot.utils.constants.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.DecimalFormat

@Parcelize
data class WifiLogsLocation(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("name") @Expose val _name: String="",
    @SerializedName("uuid") @Expose val uuid: String,
    @SerializedName("name_arabic") @Expose val name_arabic: String="",
    @SerializedName("lat") @Expose val lat: String,
    @SerializedName("lng") @Expose val lng: String,
    @SerializedName("location") @Expose val _location: String="",
    @SerializedName("location_arabic") @Expose val location_arabic: String="",
    @SerializedName("average_rating") @Expose val average_rating: Double,
    @SerializedName("provider_name") @Expose val _provider_name: String="",
    @SerializedName("provider_name_arabic") @Expose val provider_name_arabic: String="",
    @SerializedName("favourite")@Expose val favourite:Boolean


) : Parcelable

{
    val name: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) name_arabic else _name

    val location: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) location_arabic else _location


    val provider_name: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) provider_name_arabic else _provider_name
}