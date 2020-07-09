package com.ey.hotspot.ui.profile.updateprofile.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(@SerializedName("first_name") val firstName:String) {
}