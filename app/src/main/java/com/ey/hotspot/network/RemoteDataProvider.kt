package com.ey.hotspot.network

import com.ey.hotspot.ui.login.login_fragment.model.LoginRequest
import com.ey.hotspot.ui.login.login_fragment.model.LoginResponse
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
        request:LoginRequest,
        success:(LoginResponse) ->Unit,
        error: (Exception) -> Unit
    )


    suspend fun  getUserList(
        success: (JsonArray) -> Unit,
        error: (Exception) -> Unit
    )

}
