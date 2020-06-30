package com.ey.hotspot.ui.review_and_complaint.review_list

data class ReviewListModel(
    val id: Int,
    val wifiSsid: String,
    val username: String,
    val rating: Double,
    val comments: String
)