package com.ey.hotspot.network.request

import com.google.gson.annotations.SerializedName

data class TermsRequest( @SerializedName("is_tc_accepted")
                    val is_tc_accepted:Boolean?) {

}