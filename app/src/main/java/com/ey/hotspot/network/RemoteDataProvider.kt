package com.ey.hotspot.network

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
import com.google.gson.JsonArray

interface RemoteDataProvider {
    suspend fun registerUser(
        request: RegisterRequest,
        success: (RegisterResponse) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun login(
        request: LoginRequest,
        success: (LoginResponse) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun getUserList(
        success: (JsonArray) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun getProfile(
        success: (ProfileResponse) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun logout(
        success: (LogoutResponse) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun  refreshToken(
        success: (RefreshToken) -> Unit,
        error: (Exception) -> Unit
    )

    suspend fun  updateProfile(
        request:UpdateProfileRequest,
        success:(UpdateProfileResponse) ->Unit,
        error: (Exception) -> Unit
    )

}
