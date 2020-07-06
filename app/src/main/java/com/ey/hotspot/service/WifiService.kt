package com.ey.hotspot.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.os.IBinder
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.ey.hotspot.database.WifiInfoDatabase
import com.ey.hotspot.database.WifiInfoDatabaseDao
import com.ey.hotspot.database.WifiInformationTable
import com.ey.hotspot.utils.CHANNEL_ID
import com.ey.hotspot.utils.SpeedTestUtils
import com.ey.hotspot.utils.getNotification
import com.ey.hotspot.utils.wifi_notification_key
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.util.*

class WifiService : Service() {
    lateinit var connec: ConnectivityManager
    lateinit var wifiManager: WifiManager
    lateinit var database: WifiInfoDatabaseDao

    override fun onCreate() {
        super.onCreate()

        //Get connectivity Manager
        connec = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //Get Wifi Manager
        wifiManager = applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val networkRequestWiFi = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        //get instance of database
        database = WifiInfoDatabase.getInstance(application).wifiInfoDatabaseDao


        connec.registerNetworkCallback(networkRequestWiFi, networkCallbackWiFi)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val input = intent?.getStringExtra(wifi_notification_key) ?: "Checking for wifi connection"

        //Start foreground service with notification
        startForeground(
            1,
            getNotification(
                this,
                "Wifi Service",
                input,
                CHANNEL_ID
            )
        )

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private var networkCallbackWiFi = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network?) {
            Toast.makeText(
                applicationContext,
                "WiFi disconnected",
                Toast.LENGTH_SHORT
            ).show()

            //Start Service
            ContextCompat.startForegroundService(
                applicationContext,
                Intent(applicationContext, WifiService::class.java)
            )
        }

        override fun onAvailable(network: Network?) {

            //This will initially show the notification as wifi connected
            ContextCompat.startForegroundService(
                applicationContext,
                Intent(applicationContext, WifiService::class.java).apply {
                    putExtra(
                        wifi_notification_key,
                        "WiFi connected : ${wifiManager.connectionInfo.ssid}, Speed : Calculating..."
                    )
                })
            

            //TODO("Check for internet connection first")
            CoroutineScope(Dispatchers.IO).launch {
                SpeedTestUtils.calculateSpeed(
                    onCompletedReport = {
                        //get download speed
                        val downloadSpeed = ((it?.transferRateBit
                            ?: 0.toBigDecimal()) / 104857.toBigDecimal()).setScale(
                            2,
                            RoundingMode.CEILING
                        )


                        //Start Service
                        ContextCompat.startForegroundService(
                            applicationContext,
                            Intent(applicationContext, WifiService::class.java).apply {
                                putExtra(
                                    wifi_notification_key,
                                    "WiFi connected : ${wifiManager?.connectionInfo.ssid}, Speed : $downloadSpeed Mbps"
                                )
                            })

                        CoroutineScope(Dispatchers.IO).launch {
                            //Insert data into table
                            database.insert(
                                WifiInformationTable(
                                    wifiSsid = wifiManager.connectionInfo.ssid,
                                    connectedOn = Calendar.getInstance(),
                                    downloadSpeed = downloadSpeed.toString()
                                )
                            )
                        }
                    },
                    onProgressReport = {

                    },
                    onErrorReport = {
                        //Start Service
                        ContextCompat.startForegroundService(
                            applicationContext,
                            Intent(applicationContext, WifiService::class.java).apply {
                                putExtra(
                                    wifi_notification_key,
                                    "No Internet Connection"
                                )
                            })
                    }).startDownload("http://ipv4.ikoula.testdebit.info/1M.iso")
            }

        }
    }
}