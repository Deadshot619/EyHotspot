package com.ey.hotspot.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddReviewRequest(
    @SerializedName("location_id") @Expose val locationId: Int,
    @SerializedName("rating") @Expose val rating: Int,
    @SerializedName("description") @Expose val description: String
)