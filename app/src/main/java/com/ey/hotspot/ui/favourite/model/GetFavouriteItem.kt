package com.ey.hotspot.ui.favourite.model

import com.ey.hotspot.utils.LANGUAGE
import com.ey.hotspot.utils.constants.Constants
import com.google.gson.annotations.SerializedName

class GetFavouriteItem(
    @SerializedName("user_id") val user_id : Int,
    @SerializedName("id") val id : Int,
    @SerializedName("provider_id") val provider_id : Int,
    @SerializedName("hotspot_id") val hotspot_id : String,
    @SerializedName("name") val _name : String,
    @SerializedName("name_arabic") val name_arabic : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lng") val lng : Double,
    @SerializedName("location") val _location : String,
    @SerializedName("location_arabic") val location_arabic : String,
    @SerializedName("type") val _type : String,
    @SerializedName("type_arabic") val type_arabic : String,
    @SerializedName("average_rating") val average_rating : String? = "",
    @SerializedName("provider_name") val _provider_name : String,
    @SerializedName("provider_name_arabic") val provider_name_arabic : String,
    @SerializedName("navigate_url") val navigate_url : String,
    @SerializedName("favourite") val favourite : Boolean,
    @SerializedName("uuid") val uuid: String
) {

    val name: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) name_arabic else _name

    val location: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) location_arabic else _location

    val type: String?
        get() = if (LANGUAGE == Constants.ARABIC_LANG) type_arabic else _type

    val provider_name: String
        get() = if (LANGUAGE == Constants.ARABIC_LANG) provider_name_arabic else _provider_name


}