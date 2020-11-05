package com.ey.hotspot.app_core_lib

import android.app.Application
import android.content.SharedPreferences
import com.ey.hotspot.BuildConfig
import timber.log.Timber

abstract class CoreApp : Application() {

    companion object {
        lateinit var instance: CoreApp
        lateinit var sharedPreferences: SharedPreferences

        //Variable to store deep link start status
        var DL_START = 0
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }
}