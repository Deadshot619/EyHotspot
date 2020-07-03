package com.ey.hotspot.network

import com.google.gson.JsonArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object DataProvider : RemoteDataProvider {

    private val mServices: APIInterface by lazy {
        APIClient.getClient().create(APIInterface::class.java)
    }

    override suspend fun registerUser(
        /*request: Request,*/       //If there's a request
        success: (JsonArray) -> Unit,
        error: (java.lang.Exception) -> Unit
    ) {
        withContext(Dispatchers.Main) {
            try {
                val result = mServices.register(/*request*/).await()
                success(result)
            } catch (e: Exception) {
                error(e)
            }
        }
    }


    override suspend fun registration(

        success: (JsonArray) -> Unit,
        error: (java.lang.Exception) -> Unit
    ) {

        withContext(Dispatchers.Main) {
            try {
                val result = mServices.registration().await()
            } catch (e: Exception) {
                error(e)
            }
        }
    }
}