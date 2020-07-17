package com.ey.hotspot.network.response

data class ComplaintIssuesTypes(
    val types: List<Type>
)

data class Type(
    val key: String,
    val value: String
)