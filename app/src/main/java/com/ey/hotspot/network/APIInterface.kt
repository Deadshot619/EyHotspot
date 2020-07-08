package com.ey.hotspot.network

import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.RegisterResponse
import com.ey.stringlocalization.utils.Constants
import com.google.gson.JsonArray
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST


interface APIInterface {

    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Deferred<RegisterResponse>

    @POST(Constants.API_LOGIN)
    fun login():Deferred<JsonArray>

    @POST(Constants.API_NEARBYWIFILIST)
    fun  getNearByWifiList():Deferred<JsonArray>
}