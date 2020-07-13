package com.ey.hotspot.network

import com.ey.hotspot.BuildConfig
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.utils.constants.Constants.Companion.HEADER_AUTHORIZATION
import com.ey.hotspot.utils.constants.Constants.Companion.HEADER_X_LOCALIZATION
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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
        fun getClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(getUnsafeOkHttpClient().build())
                .build()
        }

        class HeaderInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                val value =
                    "${HotSpotApp.prefs?.getUserDataPref()?.tokenType} ${HotSpotApp.prefs?.getUserDataPref()?.accessToken}"

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

                if (response.code() == 401) {//Unauthorized
//                    CoroutineScope(Dispatchers.Main).launch {
                    var retry = false
                    runBlocking {
                        DataProvider.refreshToken({
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

//                    }
                }
                Timber.tag("TokenInterceptor").d(response.code().toString())
                return response
            }
        }


        fun getValidTokenInterceptor(): OkHttpClient.Builder {
            return OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)
//                val body: ResponseBody? = response.body()
//                if (body)
                Timber.tag("TokenInterceptor").d(response.code().toString())
                response
            }
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
                return builder
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }


    }
}