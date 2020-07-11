package com.ey.hotspot.ui.favourite.model

import com.google.gson.annotations.SerializedName

data class MarkFavouriteResponse(
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("location_id") val location_id: Int,
    @SerializedName("mark") val mark: Boolean
) {
}