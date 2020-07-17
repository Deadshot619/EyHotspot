package com.ey.hotspot.network.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddComplaintRequest(
    @SerializedName("location_id") @Expose val locationId: Int,
    @SerializedName("issue_type") @Expose val issueType: String,
    @SerializedName("complaint") @Expose val complaint: String
)