package com.ey.hotspot.ui.favourite.model

import com.google.gson.annotations.SerializedName

class FavouriteItem(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("last_name")
    val lastName: String
) {
}