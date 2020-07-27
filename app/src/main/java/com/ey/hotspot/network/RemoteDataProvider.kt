package com.ey.hotspot.network

import com.ey.hotspot.network.request.*
import com.ey.hotspot.network.response.*
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.login.changepassword.model.ResetPasswordRequest
import com.ey.hotspot.ui.login.changepassword.model.ResetPasswordResponse
import com.ey.hotspot.ui.login.logout.LogoutResponse
import com.ey.hotspot.ui.login.otpverification.fragment.model.SendOTPRequest
import com.ey.hotspot.ui.login.otpverification.fragment.model.VerifyOTPRequest
import com.ey.hotspot.ui.login.verifyotp.model.*
import com.ey.hotspot.ui.profile.fragment.model.ProfileResponse
import com.ey.hotspot.ui.profile.fragment.model.UpdateProfileRequest
import com.ey.hotspot.ui.registration.register_user.model.RegistrationResponse
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListRequest
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListResponse
import com.google.gson.JsonArray

interface RemoteDataProvider {
    suspend fun registerUser(
        request: RegisterRequest,
        success: (BaseResponse<RegistrationResponse>) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun login(
        request: LoginRequest,
        success: (BaseResponse<LoginResponse?>) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun getUserList(
        success: (JsonArray) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun getProfile(
        success: (BaseResponse<ProfileResponse>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun logout(
        success: (BaseResponse<LogoutResponse>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun refreshTokenAsync(
        success: (BaseResponse<LoginResponse>) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun updateProfile(
        request: UpdateProfileRequest,
        success: (BaseResponse<UpdateProfileResponse>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun getHotspot(
        request: GetHotSpotRequest,
        success: (BaseResponse<List<GetHotSpotResponse>>) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun getUserHotSpot(
        request: GetHotSpotRequest,
        success: (BaseResponse<List<GetHotSpotResponse>>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun markFavourite(
        request: MarkFavouriteRequest,
        success: (BaseResponse<MarkFavouriteResponse>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun getFavourite(
        request: GetFavoriteRequest,
        success: (BaseResponse<List<GetFavouriteItem>>) -> Unit,
        error: (Exception) -> Unit
    )

    //Reviews & Complaints
    suspend fun getReviews(     //Reviews
        success: (BaseResponse<List<ReviewsList>>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun getCompaints(   //Compaints
        success: (BaseResponse<List<ComplaintsList>>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun getLocationReviews(     //Reviews
        request: GetLocationReviewsRequest,
        success: (BaseResponse<List<ReviewsList>>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun getComplaintsIssuesTypes(
        success: (BaseResponse<ComplaintIssuesTypes>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun addReviews(
        request: AddReviewRequest,
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun addComplaints(
        request: AddComplaintRequest,
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun socialLogin(
        request: SocialLoginRequest,
        success: (BaseResponse<LoginResponse?>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun sendOTP(
        request: SendOTPRequest,
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun verifyOTP(
        request: VerifyOTPRequest,
        success: (BaseResponse<LoginResponse>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun forgotPassword(
        request: ForgotPasswordRequest,
        success: (BaseResponse<ForgotPasswordResponse>) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun forgotPasswordVerifyOTP(
        request: ForgotPasswordVerifyOTPRequest,
        success: (BaseResponse<ForgotPasswordVerifyOTPResponse>) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun resetPassword(
        request: ResetPasswordRequest,
        success: (BaseResponse<ResetPasswordResponse>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun resendForgotPasswordOTP(
        request: ForgotPasswordResendOTPRequest,
        success: (BaseResponse<ResendForgotPasswordOTP>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun getCountryCode(
        success: (BaseResponse<CoutryCode>) -> Unit,
        error: (Exception) -> Unit
    )

    //Wifi
    suspend fun validateWifi(
        request: ValidateWifiRequest,
        success: (BaseResponse<ValidateWifiResponse>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun wifiLogin(
        request: WifiLoginRequest,
        success: (BaseResponse<WifiLoginResponse>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun wifiLogout(
        request: WifiLogoutRequest,
        success: (BaseResponse<WifiLogoutResponse>) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun wifiLogList(
        request:WifiLogListRequest,
        success: (BaseResponse<List<WifiLogListResponse>>) -> Unit,
        error: (Exception) -> Unit
    )
}
