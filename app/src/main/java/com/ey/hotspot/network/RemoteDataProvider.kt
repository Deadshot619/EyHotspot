package com.ey.hotspot.network

import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.network.response.RegisterResponse
import com.google.gson.JsonArray

interface RemoteDataProvider {
    suspend fun registerUser(
        request: RegisterRequest,
        success: (RegisterResponse) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun  login(
        request: LoginRequest,
        success:(LoginResponse) ->Unit,
        error: (Exception) -> Unit
    )


    suspend fun  getUserList(
        success: (JsonArray) -> Unit,
        error: (Exception) -> Unit
    )

}
