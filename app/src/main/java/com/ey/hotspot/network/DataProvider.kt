package com.ey.hotspot.network

import com.ey.hotspot.app_core_lib.HotSpotApp
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object DataProvider : RemoteDataProvider {

    private val mServices: APIInterface by lazy {
        APIClient.getClient().create(APIInterface::class.java)
    }

    override suspend fun registerUser(
        request: RegisterRequest,      //If there's a request
        success: (BaseResponse<RegistrationResponse>) -> Unit,
        error: (java.lang.Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {
                val result = mServices.register(request).await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }


    override suspend fun login(
        request: LoginRequest,
        success: (BaseResponse<LoginResponse?>) -> Unit,
        error: (Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {
            try {
                val result = mServices.login(request).await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }


    override suspend fun getUserList(
        success: (JsonArray) -> Unit,
        error: (Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {

            try {
                val result = mServices.getUserList().await()
            } catch (e: Exception) {
                error(e)
            }
        }

    }

    override suspend fun getProfile(
        success: (BaseResponse<ProfileResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {

                val result = mServices.getProfile().await()
                success(result)

            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun logout(
        success: (BaseResponse<LogoutResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {

            try {

                val result = mServices.logOut().await()
                success(result)

            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun refreshTokenAsync(
        success: (BaseResponse<LoginResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result = mServices.refreshTokenAsync().await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun updateProfile(
        request: UpdateProfileRequest,
        success: (BaseResponse<UpdateProfileResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {
            val result =
                mServices.updateProfile(request)
                    .await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun getHotspot(
        request: GetHotSpotRequest,
        success: (BaseResponse<List<GetHotSpotResponse>>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result = mServices.getHotSpots(request).await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun getUserHotSpot(
        request: GetHotSpotRequest,
        success: (BaseResponse<List<GetHotSpotResponse>>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result = mServices.getUserHotSpot(request).await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }

    }

    override suspend fun markFavourite(
        request: MarkFavouriteRequest,
        success: (BaseResponse<MarkFavouriteResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result =
                mServices.markFavourite(request)
                    .await()

            success(result)

        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun getFavourite(
        request: GetFavoriteRequest,
        success: (BaseResponse<List<GetFavouriteItem>>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {
            val result =
                mServices.getFavouriteAsync(request).await()
            success(result)

        } catch (e: Exception) {
            error(e)
        }
    }

    /**
     * Reviews & Complaints Module
     */

    //Reviews
    override suspend fun getReviews(
        success: (BaseResponse<List<ReviewsList>>) -> Unit,
        error: (Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {
                val result = mServices.getReviewsAsync().await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    //Complaints
    override suspend fun getCompaints(
        success: (BaseResponse<List<ComplaintsList>>) -> Unit,
        error: (Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {
                val result = mServices.getComplaintsAsync().await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    //Reviews
    override suspend fun getLocationReviews(
        request: GetLocationReviewsRequest,
        success: (BaseResponse<List<ReviewsList>>) -> Unit,
        error: (Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {
                val result = mServices.getLocationReviewsAsync(request).await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }


    override suspend fun getComplaintsIssuesTypes(
        success: (BaseResponse<ComplaintIssuesTypes>) -> Unit,
        error: (Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {
                val result = mServices.fetchComplaintsIssueType().await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun addReviews(
        request: AddReviewRequest,
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {
            try {
                val result = mServices.apiAddReview(request).await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun addComplaints(
        request: AddComplaintRequest,
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {
                val result = mServices.apiAddComplaint(request).await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun socialLogin(
        request: SocialLoginRequest,
        success: (BaseResponse<LoginResponse?>) -> Unit,
        error: (Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {

            try {
                val result = mServices.socialLogin(request).await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun sendOTP(
        request: SendOTPRequest,
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {
            try {

                val result =
                    mServices.sendOTP(HotSpotApp.prefs!!.getRegistrationTempToken(), request)
                        .await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun verifyOTP(
        request: VerifyOTPRequest,
        success: (BaseResponse<LoginResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {
            val result =
                mServices.verifyOTP(HotSpotApp.prefs!!.getRegistrationTempToken(), request)
                    .await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun forgotPassword(
        request: ForgotPasswordRequest,
        success: (BaseResponse<ForgotPasswordResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {

            val result = mServices.forgotPassword(request).await()
            success(result)

        } catch (e: Exception) {
            error(e)
        }

    }

    override suspend fun forgotPasswordVerifyOTP(
        request: ForgotPasswordVerifyOTPRequest,
        success: (BaseResponse<ForgotPasswordVerifyOTPResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {
            val result = mServices.forgotPasswordVerifyOTP(
                HotSpotApp.prefs!!.getRegistrationTempToken(), request
            ).await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun resetPassword(
        request: ResetPasswordRequest,
        success: (BaseResponse<ResetPasswordResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {

            val result = mServices.resetPassword(
                HotSpotApp.prefs!!.getRegistrationTempToken(), request
            ).await()

            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun resendForgotPasswordOTP(
        request: ForgotPasswordResendOTPRequest,
        success: (BaseResponse<ResendForgotPasswordOTP>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {
            val result = mServices.resendForgotPasswordOTP(
                HotSpotApp.prefs!!.getRegistrationTempToken(), request
            ).await()

            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun getCountryCode(
        success: (BaseResponse<CoutryCode>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result = mServices.getCountryListAsync().await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }

    }

    //Wifi
    override suspend fun validateWifi(
        request: ValidateWifiRequest,
        success: (BaseResponse<ValidateWifiResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result = mServices.validateWifiAsync(request).await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }

    }

    override suspend fun wifiLogin(
        request: WifiLoginRequest,
        success: (BaseResponse<WifiLoginResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result = mServices.wifiLoginAsync(request).await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }

    }

    override suspend fun wifiLogout(
        request: WifiLogoutRequest,
        success: (BaseResponse<WifiLogoutResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result = mServices.wifiLogoutAsync(request).await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }

    }

    override suspend fun wifiLogList(
        request:WifiLogListRequest,
        success: (BaseResponse<List<WifiLogListResponse>>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {
            val result = mServices.wifiLogsList(request).await()
            success(result)

        } catch (e: Exception) {
            error(e)
        }

    }

    override suspend fun wifiSearchKeyWords(
        success: (BaseResponse<List<String>>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result = mServices.getWifiSearchKeyWordsAsync().await()
            success(result)
        } catch (e: Exception){
            error(e)
        }
    }
}