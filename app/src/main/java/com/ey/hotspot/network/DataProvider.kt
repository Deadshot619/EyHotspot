package com.ey.hotspot.network

import android.util.Log
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.login.logout.LogoutResponse
import com.ey.hotspot.ui.login.logout.RefreshToken
import com.ey.hotspot.ui.profile.fragment.model.ProfileResponse
import com.ey.hotspot.ui.profile.updateprofile.model.UpdateProfileRequest
import com.ey.hotspot.ui.profile.updateprofile.model.UpdateProfileResponse
import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object DataProvider : RemoteDataProvider {

    private val mServices: APIInterface by lazy {
        APIClient.getClient().create(APIInterface::class.java)
    }

    override suspend fun registerUser(
        request: RegisterRequest,      //If there's a request
        success: (BaseResponse<Any>) -> Unit,
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
        success: (BaseResponse<LoginResponse>) -> Unit,
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

                Log.d(
                    "GetAccessToken", HotSpotApp.prefs!!.getAccessToken()
                )
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun logout(success: (LogoutResponse) -> Unit, error: (Exception) -> Unit) {

        withContext(Dispatchers.Main) {

            try {

                val result = mServices.logOut().await()
                success(result)

            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun refreshToken(success: (RefreshToken) -> Unit, error: (Exception) -> Unit) {


        withContext(Dispatchers.Main) {
            try {

                val result = mServices.refreshToken().await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }

    override suspend fun updateProfile(
        request: UpdateProfileRequest,
        success: (BaseResponse<Any>) -> Unit,
        error: (Exception) -> Unit
    ) {

        try {
            val result = mServices.updateProfile("Bearer  "+HotSpotApp.prefs!!.getAccessToken()!!, request).await()
            success(result)
        } catch (e: Exception) {
            error(e)
        }
    }


}