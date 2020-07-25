package com.ey.hotspot.network

import com.ey.hotspot.BuildConfig
import com.ey.hotspot.network.interceptor.AuthInterceptor
import com.ey.hotspot.network.interceptor.HeaderInterceptor
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class APIClient {
    companion object {
        internal const val MAXIMUM_RETRY = 1
        internal var currentTry = 0
        internal var originalRequest: Request? = null

        //Build Retrofit Client
        fun getClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(getUnsafeOkHttpClient().build())
                .build()
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

//                builder.addNetworkInterceptor(ChuckInterceptor(CoreApp.instance))
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