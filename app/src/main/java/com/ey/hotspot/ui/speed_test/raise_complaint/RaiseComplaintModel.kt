package com.ey.hotspot.ui.speed_test.raise_complaint

data class RaiseComplaintModel(
    var id: Int = -1,
    var wifiSsid: String = "",
    var wifiProvider: String = "",
    var wifiLocation: String = "",
    var issueType: String = "",
    var feedback: String = ""
)