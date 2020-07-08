package com.ey.hotspot.network

import com.ey.hotspot.ui.registration.register_user.model.Register
import com.ey.hotspot.ui.registration.register_user.model.RegisterResponse
import com.ey.stringlocalization.utils.Constants
import com.google.gson.JsonArray
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST


interface APIInterface {

    @POST("register")
    fun register(@Body registerRequest: Register): Deferred<RegisterResponse>

    @POST(Constants.API_LOGIN)
    fun login():Deferred<JsonArray>

    @POST(Constants.API_NEARBYWIFILIST)
    fun  getNearByWifiList():Deferred<JsonArray>
}