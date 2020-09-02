package com.ey.hotspot.ui.splash_screen

import android.app.Application
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.utils.WIFI_KEYWORDS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel(application: Application): BaseViewModel(application) {

    init {
        getWifiSearchKeywords()
    }

    //Retrieve & store Wifi key words
    private fun getWifiSearchKeywords(){
        CoroutineScope(Dispatchers.Main).launch {
            DataProvider.wifiSearchKeyWords(
                success = {
                    if (it.status){
                        WIFI_KEYWORDS = it.data
                        HotSpotApp.prefs?.saveWifiKeywordsPref(it.data)
                    }
                },
                error = {
                    checkError(it)
                }
            )
        }
    }
}