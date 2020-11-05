package com.ey.hotspot.network.response

import android.os.Parcelable
import com.ey.hotspot.utils.LANGUAGE
import com.ey.hotspot.utils.constants.Constants
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValidateWifiResponse(
    @SerializedName("average_speed") @Expose val  average_speed: Double? = 0.0,
    @SerializedName("distance") @Expose val  distance: Double = 0.0,
    @SerializedName("id") @Expose val  id: Int = 0,
    @SerializedName("number_of_users_connected") @Expose val  number_of_users_connected: Int = 0,
    @SerializedName("wifi_name") @Expose val  _wifi_name: String = "-",
    @SerializedName("wifi_name_arabic") @Expose val  wifi_name_arabic: String? = "-",
    @SerializedName("city") @Expose val  _city: String? = "-",
    @SerializedName("city_arabic") @Expose val  city_arabic: String? = "-",
    @SerializedName("provider_name") @Expose val  _provider_name: String? = "-",
    @SerializedName("provider_name_arabic") @Expose val  provider_name_arabic: String? = "-"

    ): Parcelable {

    val wifi_name: String?
        get() = if (LANGUAGE == Constants.ARABIC_LANG) wifi_name_arabic else _wifi_name

    val city: String?
        get() = if (LANGUAGE == Constants.ARABIC_LANG) city_arabic else _city

    val provider_name: String?
        get() = if (LANGUAGE == Constants.ARABIC_LANG) provider_name_arabic else _provider_name
}