package com.ey.hotspot.network

import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.RegisterResponse
import com.ey.hotspot.ui.login.login_fragment.model.LoginRequest
import com.ey.hotspot.ui.registration.register_user.model.Register
import com.ey.hotspot.ui.registration.register_user.model.RegisterResponse
import com.ey.stringlocalization.utils.Constants
import com.google.gson.JsonArray
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface APIInterface {


    @POST(Constants.API_REGISTRATION)
    fun register(@Body registerRequest: RegisterRequest): Deferred<RegisterResponse>

    @POST(Constants.API_LOGIN)
    fun login(@Body loginRequest: LoginRequest): Deferred<JsonArray>

    @GET(Constants.API_GET_USER_LIST)
    fun getUserList(): Deferred<JsonArray>

    @POST(Constants.API_NEARBYWIFILIST)
    fun  getNearByWifiList():Deferred<JsonArray>
}