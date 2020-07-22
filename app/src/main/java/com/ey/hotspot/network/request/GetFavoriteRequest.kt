package com.ey.hotspot.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetFavoriteRequest (
    @SerializedName("name") @Expose val name: String = ""
)