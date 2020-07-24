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
import androidx.core.content.ContextCompat
import com.ey.hotspot.R
import com.ey.hotspot.database.WifiInfoDatabase
import com.ey.hotspot.database.WifiInfoDatabaseDao
import com.ey.hotspot.database.WifiInformationTable
import com.ey.hotspot.utils.*
import com.ey.hotspot.utils.constants.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WifiService : Service() {
    companion object{
        //Variable to indicate whether the service is running
        private var _isRunning = false
        val isRunning: Boolean
            get() = _isRunning
    }

    private lateinit var connec: ConnectivityManager
    private lateinit var wifiManager: WifiManager
    private lateinit var database: WifiInfoDatabaseDao

    private val WIFI_SERVICE_ID = 1

    private var _currentlyInsertedDataId = -1L     //Holds value of currently inserted DB data

    override fun onCreate() {
        super.onCreate()

        //indicate Service is running
        _isRunning = true

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

        val input = intent?.getStringExtra(wifi_notification_key) ?: ""

        if (input != "")
            //Start foreground service with notification
            startForeground(
                WIFI_SERVICE_ID,
                getNotification(
                    this,
                    getString(R.string.wifi_service_label),
                    input,
                    CHANNEL_ID
                )
            )

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        //indicate EService is not running
        _isRunning = false
    }

    private var networkCallbackWiFi = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network?) {
            //TODO 11/7/2020 : Remove this, once live
            /*Toast.makeText(
                applicationContext,
                getString(R.string.wifi_disconnected_label),
                Toast.LENGTH_SHORT
            ).show()*/

            //Start Service
            ContextCompat.startForegroundService(
                applicationContext,
                Intent(applicationContext, WifiService::class.java).apply {
                    putExtra(
                        wifi_notification_key,
//                                    "No Internet Connection"
                        getString(R.string.wifi_disconnected_label)
                    )
                }
            )

            //Update data in table
            CoroutineScope(Dispatchers.IO).launch {
                if (_currentlyInsertedDataId >= 0L) {   //Update data only if key is >= 0
                    val success = database.updateWifiInfoData(
                        id = _currentlyInsertedDataId,
                        disconnectedOn = Calendar.getInstance()
                    )

                    if (success == 1)       //If row updated successfully, then change current id
                        _currentlyInsertedDataId = -1
                }
            }
        }

        override fun onAvailable(network: Network?) {

            //This will initially show the notification as wifi connected
            ContextCompat.startForegroundService(
                applicationContext,
                Intent(applicationContext, WifiService::class.java).apply {
                    putExtra(
                        wifi_notification_key,
//                        "WiFi connected : ${wifiManager.connectionInfo.ssid}, Speed : Calculating..."
                        String.format(
                            getString(R.string.calculating_wifi_speed_label),
                            wifiManager.connectionInfo.ssid
                        )
                    )
                })


            //TODO("Check for internet connection first")
            CoroutineScope(Dispatchers.IO).launch {
                SpeedTestUtils.calculateSpeed(
                    onCompletedReport = {//When speed test is completed successfully
                        //get download speed
                        val downloadSpeed = it?.transferRateBit?.convertBpsToMbps()


                        //Start Service
                        ContextCompat.startForegroundService(
                            applicationContext,
                            Intent(applicationContext, WifiService::class.java).apply {
                                putExtra(
                                    wifi_notification_key,
//                                    "WiFi connected : ${wifiManager?.connectionInfo.ssid}, Speed : $downloadSpeed Mbps"
                                    String.format(
                                        getString(R.string.calculated_wifi_speed_label),
                                        wifiManager.connectionInfo.ssid,
                                        downloadSpeed
                                    )
                                )
                            })

                        //Insert data into table
                        CoroutineScope(Dispatchers.IO).launch {
                            _currentlyInsertedDataId = database.insert(
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
//                                    "No Internet Connection"
                                    getString(R.string.no_internet_connection_label)
                                )
                            })

                        //Insert data into table
                        CoroutineScope(Dispatchers.IO).launch {
                            _currentlyInsertedDataId = database.insert(
                                WifiInformationTable(
                                    wifiSsid = wifiManager.connectionInfo.ssid,
                                    connectedOn = Calendar.getInstance(),
                                    downloadSpeed = "0"
                                )
                            )
                        }
                    }).startDownload(Constants.DOWNLOAD_LINK)
            }

        }
    }
}