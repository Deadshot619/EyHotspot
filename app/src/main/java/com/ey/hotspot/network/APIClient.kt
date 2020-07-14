package com.ey.hotspot.network

import android.content.Intent
import com.ey.hotspot.BuildConfig
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.utils.constants.Constants.Companion.API_REFRESH_TOKEN
import com.ey.hotspot.utils.constants.Constants.Companion.HEADER_AUTHORIZATION
import com.ey.hotspot.utils.constants.Constants.Companion.HEADER_X_LOCALIZATION
import com.ey.hotspot.utils.constants.Constants.Companion.UNAUTHORIZED
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import kotlinx.coroutines.runBlocking
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class APIClient {
    companion object {
        private const val MAXIMUM_RETRY = 1
        private var currentTry = 0
        private var originalRequest: Request? = null

        fun getClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(getUnsafeOkHttpClient().build())
                .build()
        }

        //Add header to requests
        class HeaderInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                val value = ""
//                    "${HotSpotApp.prefs?.getUserDataPref()?.tokenType} ${HotSpotApp.prefs?.getUserDataPref()?.accessToken}"

                if (!request.url().toString().contains(UNAUTHORIZED) && !request.url().toString().contains(API_REFRESH_TOKEN)) {
                    originalRequest = request
                    currentTry = 0
                }

                return try {
                    request = request.newBuilder()
                        .header(HEADER_AUTHORIZATION, value)    //Add Auth header
                        .header(HEADER_X_LOCALIZATION, "${HotSpotApp.prefs?.getLanguage()}")
                        .build()

                    chain.proceed(request)
                } catch (e: Exception) {
                    Timber.tag("TokenInterceptor: Error").d(e.message.toString())
                    chain.proceed(request)
                }
            }
        }

        //Check if token is Valid/Invalid
        class ValidTokenInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val response = chain.proceed(request)

                if (currentTry < MAXIMUM_RETRY) {
                    if (response.code() == 401) {//Unauthorized
                        currentTry = 0
                        var retry = false
                        runBlocking {
                            DataProvider.refreshTokenAsync({
                                if (it.status) {
                                    HotSpotApp.prefs?.setUserDataPref(it.data)
                                    Timber.tag("TokenInterceptor : New").d(it.data.accessToken)

                                    retry = true
                                }
                            }, {
                                Timber.tag("TokenInterceptor").d(it.message.toString())
                            })
                        }

                        if (retry) {
                            val newRequest = request.newBuilder().header(
                                HEADER_AUTHORIZATION,
                                "${HotSpotApp.prefs?.getUserDataPref()?.tokenType} ${HotSpotApp.prefs?.getAccessToken()}"
                            ).build()
                            retry = false
                            return chain.proceed(newRequest)
                        }

                    }
                    currentTry++
                }

                Timber.tag("TokenInterceptor").d(response.code().toString())
                return response
            }
        }

        class AuthInterceptor : Authenticator {
            override fun authenticate(route: Route?, response: Response): Request? {
                if (response.code() == 401) {
                    if (currentTry < MAXIMUM_RETRY) {
                        var success = false
                        currentTry++

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


                        return if (success)
                            originalRequest?.newBuilder()?.header(
                                HEADER_AUTHORIZATION,
                                "${HotSpotApp.prefs?.getUserDataPref()?.tokenType} ${HotSpotApp.prefs?.getAccessToken()}"
                            )?.build()
                        else{
                            returnToLoginScreen()
                            null
                        }
                    }
                }
                return null
            }
        }


        private fun returnToLoginScreen(){
            //Clear Data
            HotSpotApp.prefs?.deleteUserData()

            //Redirect user to Login Activity
            CoreApp.instance.startActivity(Intent(CoreApp.instance, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
        }

        private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {

            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }


                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }
                })

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory

                val builder = OkHttpClient.Builder()
//                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
//                builder.hostnameVerifier { hostname, session -> true }
//                builder.addNetworkInterceptor(StethoInterceptor())
                builder.addInterceptor(HeaderInterceptor())

//                builder.addInterceptor(ValidTokenInterceptor())
                builder.addNetworkInterceptor(ChuckInterceptor(CoreApp.instance))
                builder.connectTimeout(1, TimeUnit.MINUTES)
                builder.readTimeout(30, TimeUnit.SECONDS)
                builder.writeTimeout(15, TimeUnit.SECONDS)
                builder.authenticator(AuthInterceptor())
                return builder
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }


    }
}