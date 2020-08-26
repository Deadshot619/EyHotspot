package com.ey.hotspot.app_core_lib

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.multidex.MultiDex
import com.crashlytics.android.BuildConfig
import com.crashlytics.android.Crashlytics
import com.ey.hotspot.service.WifiService
import com.ey.hotspot.utils.LanguageManager
import com.ey.hotspot.utils.MyHotSpotSharedPreference
import com.ey.hotspot.utils.constants.Constants
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.setAutoLogAppEventsEnabled
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import io.fabric.sdk.android.Fabric

class HotSpotApp : CoreApp() {

    companion object {
        var prefs: MyHotSpotSharedPreference? = null
    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate() {

        super.onCreate()
        prefs = MyHotSpotSharedPreference(applicationContext)
        LanguageManager.setLanguage(applicationContext, prefs?.getLanguage())

        FacebookSdk.fullyInitialize();
        setAutoLogAppEventsEnabled(true);

        FirebaseApp.initializeApp(getApplicationContext());

        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.setAnalyticsCollectionEnabled(true)

        val fabric = Fabric.Builder(this)
            .kits(Crashlytics())
            .debuggable(BuildConfig.DEBUG) // Enables Crashlytics debugger
            .build()
        Fabric.with(fabric)

        //Set global variable Language of app as its default to 'en'
        prefs?.setLanguage(prefs?.getLanguage() ?: Constants.ENGLISH_LANG)

        WifiService.callingLoginApiFromSpeedTest = false

        // FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
    }


    override fun attachBaseContext(base: Context?) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {

            if (base != null) {

                CoreApp.sharedPreferences =
                    androidx.preference.PreferenceManager.getDefaultSharedPreferences(base)
                prefs = MyHotSpotSharedPreference(base)

                super.attachBaseContext(LanguageManager.setLanguage(base, prefs?.getLanguage()))

            } else
                super.attachBaseContext(base)
        } else {
            super.attachBaseContext(base)
        }
        MultiDex.install(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LanguageManager.setLanguage(applicationContext, prefs?.getLanguage())
    }
}