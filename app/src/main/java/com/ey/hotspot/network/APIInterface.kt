package com.ey.hotspot.network

import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.network.response.RegisterResponse
import com.ey.hotspot.ui.login.logout.LogoutResponse
import com.ey.hotspot.ui.login.logout.RefreshToken
import com.ey.hotspot.ui.profile.fragment.model.ProfileResponse
import com.ey.hotspot.ui.profile.fragment.model.Success
import com.ey.hotspot.ui.profile.updateprofile.model.UpdateProfileRequest
import com.ey.hotspot.ui.profile.updateprofile.model.UpdateProfileResponse
import com.ey.hotspot.utils.constants.Constants
import com.google.gson.JsonArray
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface APIInterface {


    @POST(Constants.API_REGISTRATION)
    fun register(@Body registerRequest: RegisterRequest): Deferred<RegisterResponse>

    @POST(Constants.API_LOGIN)
    fun login(@Body loginRequest: LoginRequest): Deferred<LoginResponse>

    @GET(Constants.API_GET_USER_LIST)
    fun getUserList(): Deferred<JsonArray>


    @POST(Constants.API_GET_PROFILE)
    fun getProfile(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken()
    ): Deferred<ProfileResponse>


    @POST(Constants.API_LOGOUT)
    fun logOut(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken()
    ): Deferred<LogoutResponse>


    @POST(Constants.API_REFRESH_TOKEN)
    fun refreshToken(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken()
    ): Deferred<RefreshToken>


    @POST(Constants.API_UPDATE_PROFILE)
    fun updateProfile(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken(),
        @Body updateProfileRequest: UpdateProfileRequest
    ):Deferred<UpdateProfileResponse>
}