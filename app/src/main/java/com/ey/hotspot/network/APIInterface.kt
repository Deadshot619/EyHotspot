package com.ey.hotspot.network

import com.google.gson.JsonArray
import kotlinx.coroutines.Deferred
import retrofit2.http.POST


interface APIInterface {

    @POST("register")
    fun register(): Deferred<JsonArray>
}