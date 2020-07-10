package com.ey.hotspot.ui.profile.updateprofile.model

import com.ey.hotspot.utils.validations.ErrorDataClass
import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(    @SerializedName("data") val data: ErrorDataClass
) {
}