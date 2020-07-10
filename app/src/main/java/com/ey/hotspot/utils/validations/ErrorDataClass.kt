package com.ey.hotspot.utils.validations

import com.google.gson.annotations.SerializedName

data class ErrorDataClass (@SerializedName("mobile_no") val mobile_no : List<String>,
                           @SerializedName("email") val email : List<String>
){
}