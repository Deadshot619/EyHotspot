package com.ey.hotspot.database

import com.ey.hotspot.database.wifi_info.WifiInfoDatabaseDao
import com.ey.hotspot.database.wifi_info.WifiInformationTable

object DatabaseDataProvider {
    fun WifiInfoDatabaseDao.insertIntoWifiInfo(wifiInformationTable: WifiInformationTable): Long{
            return this@insertIntoWifiInfo.insert(wifiInformationTable)
    }
}