package com.ey.hotspot.network

import android.app.DownloadManager
import com.ey.hotspot.ui.registration.register_user.model.Register
import com.ey.hotspot.ui.registration.register_user.model.RegisterResponse
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object DataProvider : RemoteDataProvider {

    private val mServices: APIInterface by lazy {
        APIClient.getClient().create(APIInterface::class.java)
    }

    override suspend fun registerUser(
        request: Register,      //If there's a request
        success: (RegisterResponse) -> Unit,
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

        success: (JsonArray) -> Unit,
        error: (java.lang.Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {
            try {
                val result = mServices.login().await()
            } catch (e: Exception) {
                error(e)
            }
        }
    }


    override suspend fun getNearbyWifiList(
        success: (JsonArray) -> Unit,
        error: (Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {

            try {
                val result = mServices.getNearByWifiList().await()
            } catch (e: Exception) {
                error(e)
            }
        }

    }
}