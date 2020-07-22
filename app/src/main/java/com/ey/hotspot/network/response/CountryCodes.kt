package com.ey.hotspot.network.response

import com.google.gson.annotations.SerializedName

data class CountryCodes(
    @SerializedName("key") val key: Int,
    @SerializedName("value") val value: String
) {
}