package com.ey.hotspot.network

import com.ey.stringlocalization.utils.Constants
import com.google.gson.JsonArray
import kotlinx.coroutines.Deferred
import retrofit2.http.POST


interface APIInterface {

    @POST(Constants.API_LOGIN)
    fun register(): Deferred<JsonArray>

    @POST(Constants.API_REGISTRATION)
    fun registration():Deferred<JsonArray>
}