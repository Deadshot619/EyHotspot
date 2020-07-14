package com.ey.hotspot.network

import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.request.SocialLoginRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.home.models.GetUserHotSpotResponse
import com.ey.hotspot.ui.login.logout.LogoutResponse
import com.ey.hotspot.ui.profile.fragment.model.ProfileResponse
import com.ey.hotspot.ui.profile.fragment.model.UpdateProfileRequest
import com.ey.hotspot.ui.registration.register_user.model.RegistrationResponse
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

    override fun refreshToken(
        success: (BaseResponse<LoginResponse>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result: BaseResponse<LoginResponse> = mServices.refreshToken()
            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun updateProfile(
        request: UpdateProfileRequest,
        success: (BaseResponse<Any>) -> Unit,
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
            val result =
                mServices.getHotSpots(request)
                    .await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }

    override suspend fun getUserHotSpot(
        request: GetHotSpotRequest,
        success: (BaseResponse<List<GetUserHotSpotResponse>>) -> Unit,
        error: (Exception) -> Unit
    ) {
        try {
            val result = mServices.getUserHotSpot(

                request
            ).await()
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
        success: (BaseResponse<List<GetFavouriteItem>>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {
            val result =
                mServices.getFavourite().await()
            success(result)

        } catch (e: Exception) {
            error(e)
        }
    }

    /**
     * Reviews & Complaints Module
     */
    override suspend fun getReviewsAndComplaints(
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {
                val result = mServices.fetchReviewsAndComplaints().await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun getComplaintsIssuesTypes(
        success: (BaseResponse<Any>) -> Unit,
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
//        request:
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {
            try {
                val result = mServices.apiAddReview().await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun addComplaints(
//        request:
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {
                val result = mServices.apiAddComplaint().await()
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


}