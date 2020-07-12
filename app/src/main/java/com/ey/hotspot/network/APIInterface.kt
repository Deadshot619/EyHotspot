package com.ey.hotspot.network

import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.home.models.GetUserHotSpotResponse
import com.ey.hotspot.ui.login.logout.LogoutResponse
import com.ey.hotspot.ui.login.logout.RefreshToken
import com.ey.hotspot.ui.profile.fragment.model.ProfileResponse
import com.ey.hotspot.ui.profile.fragment.model.UpdateProfileRequest
import com.ey.hotspot.utils.constants.Constants
import com.google.gson.JsonArray
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface APIInterface {


    @POST(Constants.API_REGISTRATION)
    fun register(@Body registerRequest: RegisterRequest): Deferred<BaseResponse<Any>>

    @POST(Constants.API_LOGIN)
    fun login(@Body loginRequest: LoginRequest): Deferred<BaseResponse<LoginResponse>>

    @GET(Constants.API_GET_USER_LIST)
    fun getUserList(): Deferred<JsonArray>


    @GET(Constants.API_GET_PROFILE)
    fun getProfile(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken()
    ): Deferred<BaseResponse<ProfileResponse>>


    @POST(Constants.API_LOGOUT)
    fun logOut(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken()
    ): Deferred<BaseResponse<LogoutResponse>>


    @POST(Constants.API_REFRESH_TOKEN)
    fun refreshToken(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken()
    ): Deferred<BaseResponse<RefreshToken>>


    @POST(Constants.API_UPDATE_PROFILE)
    fun updateProfile(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken(),
        @Body updateProfileRequest: UpdateProfileRequest
    ): Deferred<BaseResponse<Any>>


    @POST(Constants.API_GET_HOTPSOT)
    fun getHotSpots(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken(),
        @Body getHotSpotRequest: GetHotSpotRequest
    ): Deferred<BaseResponse<List<GetHotSpotResponse>>>

    @POST(Constants.API_GET_USER_HOTSPOT_LIST)
    fun getUserHotSpot(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken(),
        @Body getHotSpotRequest: GetHotSpotRequest
    ): Deferred<BaseResponse<List<GetUserHotSpotResponse>>>


    @POST(Constants.API_MARK_FAVOURITE)
    fun markFavourite(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken(),
        @Body markFavouriteRequest: MarkFavouriteRequest
    ): Deferred<BaseResponse<MarkFavouriteResponse>>


    @POST(Constants.API_FAVOURITE_LIST)
    fun  getFavourite(
        @Header("Authorization") token: String = "Bearer  " + HotSpotApp.prefs!!.getAccessToken()
    ):Deferred<BaseResponse<List<GetFavouriteItem>>>

//    Reviews & Complaints
    @POST(Constants.API_GET_REVIEWS_AND_COMPLAINTS)
    fun fetchReviewsAndComplaints() : Deferred<BaseResponse<Any>>

    @POST(Constants.API_GET_COMPLAINTS_ISSUE_TYPES)
    fun fetchComplaintsIssueType() : Deferred<BaseResponse<Any>>

    @POST(Constants.API_ADD_REVIEW)
    fun apiAddReview(): Deferred<BaseResponse<Any>>

    @POST(Constants.API_ADD_COMPLAINT)
    fun apiAddComplaint(): Deferred<BaseResponse<Any>>


}