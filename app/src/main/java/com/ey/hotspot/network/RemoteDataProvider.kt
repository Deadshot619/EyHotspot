package com.ey.hotspot.network

import android.app.DownloadManager
import com.ey.hotspot.ui.registration.register_user.model.Register
import com.ey.hotspot.ui.registration.register_user.model.RegisterResponse
import com.google.gson.JsonArray
import com.google.gson.JsonObject

interface RemoteDataProvider {
    suspend fun registerUser(
        request: Register,
        success: (RegisterResponse) -> Unit,
        error: (Exception) -> Unit
    )


    suspend fun  login(

        sucess:(JsonArray) ->Unit,
        error: (Exception) -> Unit
    )


    suspend fun  getNearbyWifiList(
        success: (JsonArray) -> Unit,
        error: (Exception) -> Unit
    )
}
