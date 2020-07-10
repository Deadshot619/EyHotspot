package com.ey.hotspot.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
     @SerializedName("status")  @Expose  val status: Boolean ,
    @SerializedName("message")  @Expose val message: String,
    @SerializedName("data")  @Expose val data: T
) {
}
