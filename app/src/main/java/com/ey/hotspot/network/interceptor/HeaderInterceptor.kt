package com.ey.hotspot.network.interceptor

import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.APIClient
import com.ey.hotspot.utils.constants.Constants
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

//Add header to requests
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        //Get Access Token
        val value = HotSpotApp.prefs?.getAccessTypeAndToken() ?: ""

        /**
         *  Since the URL is redirecting us to another URL when authentication is failed, we have to check whether the current URL
         *  has Authorization/RefreshToken in it. If No, then store the current request in [APIClient.originalRequest] variable & set
         *  [APIClient.originalRequest] to 0
         */
        if (!request.url().toString().contains(Constants.UNAUTHORIZED) && !request.url().toString()
                .contains(Constants.API_REFRESH_TOKEN)
        ) {
            APIClient.originalRequest = request
            APIClient.currentTry = 0
        }

        //Add Headers to request
        return try {
            request = request.newBuilder()
                .header(Constants.HEADER_AUTHORIZATION, value)    //Add Auth header
                .header(
                    Constants.HEADER_X_LOCALIZATION,
                    "${HotSpotApp.prefs?.getLanguage()}"
                )  //Set current Language
                .build()

            chain.proceed(request)
        } catch (e: Exception) {
            Timber.tag("TokenInterceptor: Error").d(e.message.toString())
            chain.proceed(request)
        }
    }
}