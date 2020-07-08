package com.ey.hotspot.network

import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.RegisterResponse
import com.google.gson.JsonArray

interface RemoteDataProvider {
    suspend fun registerUser(
        request: RegisterRequest,
        success: (RegisterResponse) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun  login(

        success:(JsonArray) ->Unit,
        error: (Exception) -> Unit
    )


    suspend fun  getNearbyWifiList(
        success: (JsonArray) -> Unit,
        error: (Exception) -> Unit
    )
}
