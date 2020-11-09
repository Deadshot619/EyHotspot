package com.ey.hotspot.database.wifi_info

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface WifiInfoDatabaseDao {

    @Insert
    fun insert(wifiInformationTable: WifiInformationTable): Long

    @Query("SELECT * FROM wifi_information_table ORDER BY id DESC")
    fun getLastInsertedData(): List<WifiInformationTable>

    @Query("UPDATE wifi_information_table SET disconnected_on = :disconnectedOn WHERE id = :id")
    fun updateWifiInfoData(id: Long, disconnectedOn: Calendar): Int

    @Query("UPDATE wifi_information_table SET synced = :sync WHERE id = :id")
    fun updateSyncStatus(id: Long, sync: Boolean)

    @Query("SELECT * FROM wifi_information_table")
    fun getAllWifiInfoData(): List<WifiInformationTable>

    @Query("DELETE FROM wifi_information_table")
    fun deleteAllDataFromDb()
}