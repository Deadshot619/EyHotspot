package com.ey.hotspot.network.response

import com.google.gson.annotations.SerializedName

data class CoutryCode(
    @SerializedName("country_codes") val country_codes : List<CountryCodes>
)
data class CountryCodes(
    @SerializedName("key") val key: Int,
    @SerializedName("value") val value: String
)