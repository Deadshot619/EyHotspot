package com.ey.hotspot.network

import com.ey.hotspot.ui.registration.register_user.model.Register
import com.ey.hotspot.ui.registration.register_user.model.RegisterResponse
import com.google.gson.JsonArray

interface RemoteDataProvider {
    suspend fun registerUser(
        request: Register,
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
