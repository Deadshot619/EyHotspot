package com.ey.hotspot.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WifiInfoDatabaseDao {

    @Insert
    fun insert(wifiInformationTable: WifiInformationTable): Long

    @Query("SELECT * FROM wifi_information_table")
    fun getAllWifiInfoData(): List<WifiInformationTable>

}