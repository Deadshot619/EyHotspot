package com.ey.hotspot.network.interceptor

import android.content.Intent
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.APIClient
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.utils.constants.Constants
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber

class AuthInterceptor : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        //If request code is 401, that means user's token has expired
        if (response.code() == 401) {
            //We have to try to Refresh token only once
            if (APIClient.currentTry < APIClient.MAXIMUM_RETRY) {
                var success = false
                APIClient.currentTry++

                //Block current thread so that RefreshToken Api is only called once
                runBlocking {
                    DataProvider.refreshTokenAsync(
                        {
                            success = if (it.status) {
                                HotSpotApp.prefs?.setUserDataPref(it.data)
                                Timber.tag("TokenInterceptor : New").d(it.data.refreshToken)
                                true
                            } else {
                                false
                            }
                        },
                        {
                            Timber.tag("TokenInterceptor").d(it.message.toString())
                            success = false
                        })
                }

                //If token is refreshed successfully, call original request again. Else return to login screen
                return if (success)
                    APIClient.originalRequest?.newBuilder()?.header(
                        Constants.HEADER_AUTHORIZATION,
                        HotSpotApp.prefs?.getAccessTypeAndToken() ?: ""
                    )?.build()
                else {
                    returnToLoginScreen()
                    null
                }
            }
        }
        return null
    }

    //Method to redirect user to login screen
    private fun returnToLoginScreen() {
        //Clear Data
        HotSpotApp.prefs?.deleteUserData()

        //Redirect user to Login Activity
        CoreApp.instance.startActivity(Intent(CoreApp.instance, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(Constants.AUTHENTICATION_FAILED, true)
        })
    }
}