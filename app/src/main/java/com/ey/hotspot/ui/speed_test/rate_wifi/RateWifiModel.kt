package com.ey.hotspot.ui.speed_test.rate_wifi

data class RateWifiModel(
    var wifiSsid: String = "",
    var wifiProvider: String = "",
    var wifiLocation: String = "",
    var rating: Float = 0f,
    var feedback: String = ""
)