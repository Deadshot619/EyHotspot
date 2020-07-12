package com.ey.hotspot.app_core_lib

import android.app.Application
import android.content.SharedPreferences
import com.ey.hotspot.BuildConfig
import timber.log.Timber

abstract class CoreApp : Application() {

    companion object {
        lateinit var instance: CoreApp
        lateinit var sharedPreferences: SharedPreferences
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)

        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

    }
}