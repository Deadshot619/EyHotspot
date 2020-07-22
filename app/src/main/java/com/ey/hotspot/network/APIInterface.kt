package com.ey.hotspot.network

import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.request.*
import com.ey.hotspot.network.response.*
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.login.logout.LogoutResponse
import com.ey.hotspot.ui.login.otpverification.fragment.model.SendOTPRequest
import com.ey.hotspot.ui.login.otpverification.fragment.model.VerifyOTPRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordResponse
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordVerifyOTPRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordVerifyOTPResponse
import com.ey.hotspot.ui.profile.fragment.model.ProfileResponse
import com.ey.hotspot.ui.profile.fragment.model.UpdateProfileRequest
import com.ey.hotspot.ui.registration.register_user.model.RegistrationResponse
import com.ey.hotspot.utils.constants.Constants
import com.google.gson.JsonArray
import kotlinx.coroutines.Deferred
import retrofit2.http.*


interface APIInterface {


    @POST(Constants.API_REGISTRATION)
    fun register(@Body registerRequest: RegisterRequest): Deferred<BaseResponse<RegistrationResponse>>

    @POST(Constants.API_LOGIN)
    fun login(@Body loginRequest: LoginRequest): Deferred<BaseResponse<LoginResponse?>>

    @POST(Constants.API_SOCIAL_LOGIN)
    fun socialLogin(@Body socialLoginRequest: SocialLoginRequest): Deferred<BaseResponse<LoginResponse?>>

    @GET(Constants.API_GET_USER_LIST)
    fun getUserList(): Deferred<JsonArray>


    @GET(Constants.API_GET_PROFILE)
    fun getProfile(
    ): Deferred<BaseResponse<ProfileResponse>>


    @POST(Constants.API_LOGOUT)
    fun logOut(
    ): Deferred<BaseResponse<LogoutResponse>>


    //Refresh Token
    @POST(Constants.API_REFRESH_TOKEN)
    fun refreshTokenAsync(
        @Header(Constants.HEADER_REFRESH_TOKEN) token: String = HotSpotApp.prefs?.getUserDataPref()?.refreshToken.toString()
    ): Deferred<BaseResponse<LoginResponse>>


    @POST(Constants.API_UPDATE_PROFILE)
    fun updateProfile(
        @Body updateProfileRequest: UpdateProfileRequest
    ): Deferred<BaseResponse<Any>>


    @POST(Constants.API_GET_HOTPSOT)
    fun getHotSpots(
        @Body getHotSpotRequest: GetHotSpotRequest
    ): Deferred<BaseResponse<List<GetHotSpotResponse>>>

    @POST(Constants.API_GET_USER_HOTSPOT_LIST)
    fun getUserHotSpot(
        @Body getHotSpotRequest: GetHotSpotRequest
    ): Deferred<BaseResponse<List<GetHotSpotResponse>>>


    @POST(Constants.API_MARK_FAVOURITE)
    fun markFavourite(
        @Body markFavouriteRequest: MarkFavouriteRequest
    ): Deferred<BaseResponse<MarkFavouriteResponse>>


    @POST(Constants.API_FAVOURITE_LIST)
    fun getFavouriteAsync(@Body request: GetFavoriteRequest): Deferred<BaseResponse<List<GetFavouriteItem>>>

    //    Reviews & Complaints
    @GET(Constants.API_GET_REVIEWS)    //Reviews
    fun getReviewsAsync(): Deferred<BaseResponse<List<ReviewsList>>>

    @GET(Constants.API_GET_COMPLAINTS)    //Complaints
    fun getComplaintsAsync(): Deferred<BaseResponse<List<ComplaintsList>>>


    @GET(Constants.API_GET_COMPLAINTS_ISSUE_TYPES)
    fun fetchComplaintsIssueType(): Deferred<BaseResponse<ComplaintIssuesTypes>>

    @POST(Constants.API_ADD_REVIEW)
    fun apiAddReview(@Body request: AddReviewRequest): Deferred<BaseResponse<Any>>

    @POST(Constants.API_ADD_COMPLAINT)
    fun apiAddComplaint(@Body request: AddComplaintRequest): Deferred<BaseResponse<Any>>

    //OTP
    @POST(Constants.API_SEND_OTP + "/" + "{id}?")
    fun sendOTP(
        @Path("id") id: String?, @Body sendOTPRequest: SendOTPRequest
    ): Deferred<BaseResponse<Any>>

    @POST(Constants.API_VERIFY_OTP + "/" + "{id}?")
    fun verifyOTP(
        @Path("id") id: String?, @Body verifyOTPRequest: VerifyOTPRequest
    ): Deferred<BaseResponse<Any>>


    @POST(Constants.API_FORGOT_PASSWORD)
    fun forgotPassword(
        @Body forgotPasswordRequest: ForgotPasswordRequest
    ): Deferred<BaseResponse<ForgotPasswordResponse>>


    @POST(Constants.API_FORGOT_PASSWORD_VERIFY_OTP+"/"+"{id}?")
    fun forgotPasswordVerifyOTP(
        @Path("id") id:String?,

        @Body forgotPasswordVerifyOTPRequest: ForgotPasswordVerifyOTPRequest
    ): Deferred<BaseResponse<ForgotPasswordVerifyOTPResponse>>
}