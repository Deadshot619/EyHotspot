package com.ey.hotspot.ui.profile.fragment.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(@SerializedName("success") val success: Success) {
}