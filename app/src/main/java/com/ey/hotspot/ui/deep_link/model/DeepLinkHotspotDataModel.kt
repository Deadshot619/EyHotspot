package com.ey.hotspot.ui.deep_link.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeepLinkHotspotDataModel(
    val id: Int,
    val lat: Double,
    val lon: Double
): Parcelable