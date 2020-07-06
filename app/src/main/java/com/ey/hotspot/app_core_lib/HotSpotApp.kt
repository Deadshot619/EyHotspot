package com.ey.hotspot.app_core_lib

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.multidex.MultiDex
import com.ey.hotspot.utils.MyHotSpotSharedPreference
import com.ey.stringlocalization.utils.LanguageManager

class HotSpotApp :CoreApp() {

    companion object {
        var prefs: MyHotSpotSharedPreference? = null
    }

    override fun onCreate() {

        super.onCreate()
        prefs = MyHotSpotSharedPreference(applicationContext)
        LanguageManager.setLanguage(applicationContext, prefs?.getLanguage())

    }


    override fun attachBaseContext(base: Context?) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {

            if (base != null) {

                CoreApp.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(base)
                prefs = MyHotSpotSharedPreference(base)

                Log.d("Langu attachBase", prefs?.getLanguage())
                super.attachBaseContext(LanguageManager.setLanguage(base, prefs?.getLanguage()))

            }else
                super.attachBaseContext(base)
        }
        else {
            super.attachBaseContext(base)
        }
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d("on configuration", prefs?.getLanguage())
        LanguageManager.setLanguage(applicationContext, prefs?.getLanguage())
    }
}