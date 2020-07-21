package com.ey.hotspot.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComplaintsList(
    @SerializedName("created_at") @Expose val created_at: String,
    @SerializedName("description") @Expose val description: String,
    @SerializedName("diff_for_human") @Expose val diff_for_human: String,
    @SerializedName("firstname") @Expose val firstname: String?,
    @SerializedName("firstname_arabic") @Expose val firstname_arabic: String?,
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("issue_type") @Expose val issue_type: String,
    @SerializedName("lastname") @Expose val lastname: String?,
    @SerializedName("lastname_arabic") @Expose val lastname_arabic: String?,
    @SerializedName("location") @Expose val location: String,
    @SerializedName("location_arabic") @Expose val location_arabic: String,
    @SerializedName("location_name") @Expose val location_name: String,
    @SerializedName("location_name_arabic") @Expose val location_name_arabic: String,
    @SerializedName("provider_name") @Expose val provider_name: String,
    @SerializedName("provider_name_arabic") @Expose val provider_name_arabic: String
)