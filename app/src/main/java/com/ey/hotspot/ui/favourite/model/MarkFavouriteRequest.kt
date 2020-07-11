package com.ey.hotspot.ui.favourite.model

import com.google.gson.annotations.SerializedName

data class MarkFavouriteRequest(
    @SerializedName("location_id") val locationId: Int
) {
}