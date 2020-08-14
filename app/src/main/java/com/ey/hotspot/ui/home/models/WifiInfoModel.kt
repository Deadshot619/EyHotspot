package com.ey.hotspot.ui.home.models

data class WifiInfoModel(
    val id: Int = -1,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val wifiSsid: String = "",
    val providerName: String = "",
    val average_rating: String? = "",
    val location: String = "",
    val favourite: Boolean = false,
    val navigate_url: String = "",
    val uuid: String
)

fun MyClusterItems.toWifiInfoModel() = WifiInfoModel(
    id = this.mItemID,
    wifiSsid = this.title!!,
    providerName = this.snippet!!,
    lat = this.mLat,
    lon = this.mLng,
    favourite = this.mIsFavourite,
    navigate_url = this.mNavigateURL,
    location = this.mAddress,
    average_rating = this.rating,
    uuid = this.uuid
)

fun WifiInfoModel.toMyClusterItems() = MyClusterItems(
    lat = this.lat,
    lng = this.lon,
    title = this.wifiSsid,
    snippet = this.providerName,
    itemId = this.id,
    isfavourite = this.favourite,
    navigateURL = this.navigate_url,
    address = this.location,
    rating = this.average_rating,
    uuid = this.uuid
)
