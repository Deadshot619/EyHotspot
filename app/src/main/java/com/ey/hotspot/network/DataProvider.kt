package com.ey.hotspot.network

import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.network.response.RegisterResponse
import android.app.DownloadManager
import com.ey.hotspot.ui.login.login_fragment.model.LoginRequest
import com.ey.hotspot.ui.login.login_fragment.model.LoginResponse
import com.ey.hotspot.ui.registration.register_user.model.Register
import com.ey.hotspot.ui.registration.register_user.model.RegisterResponse
import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object DataProvider : RemoteDataProvider {

    private val mServices: APIInterface by lazy {
        APIClient.getClient().create(APIInterface::class.java)
    }

    override suspend fun registerUser(
        request: RegisterRequest,      //If there's a request
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
        request: LoginRequest,
        success: (LoginResponse) -> Unit,
        error: (java.lang.Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {
            try {
                val result = mServices.login(request).await()
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
}