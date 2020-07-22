package com.ey.hotspot.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetLocationReviewsRequest(
    @SerializedName("location_id") @Expose val locationId: Int
)