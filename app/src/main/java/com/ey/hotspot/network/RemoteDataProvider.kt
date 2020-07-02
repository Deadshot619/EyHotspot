package com.ey.hotspot.network

import com.google.gson.JsonArray

interface RemoteDataProvider {
    suspend fun registerUser(
        success: (JsonArray) -> Unit,
        error: (Exception) -> Unit
    )
}
