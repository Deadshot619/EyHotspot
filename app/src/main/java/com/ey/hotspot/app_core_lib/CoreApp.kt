package com.ey.hotspot.app_core_lib

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class CoreApp : Application() {

    companion object {
        lateinit var instance: CoreApp
        lateinit var sharedPreferences: SharedPreferences
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    }
}