package com.ey.hotspot.database.wifi_info

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "wifi_information_table")
data class WifiInformationTable(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "wifi_ssid")
    val wifiSsid: String,

    @ColumnInfo(name = "connected_on")
    val connectedOn: Calendar,

    @ColumnInfo(name = "disconnected_on")
    val disconnectedOn: Calendar? = null,

    @ColumnInfo(name = "download_speed")
    val downloadSpeed: String,

    @ColumnInfo(name = "wifi_id")
    val wifiId: Int,

    @ColumnInfo(name = "synced")
    val synced: Boolean = false,

    @ColumnInfo(name = "access_token")
    val accessToken: String?
    
/*    @ColumnInfo(name = "upload_speed")
    val uploadSpeed: String*/
)