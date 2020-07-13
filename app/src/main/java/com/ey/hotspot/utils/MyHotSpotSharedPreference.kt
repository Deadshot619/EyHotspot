package com.ey.hotspot.utils

import android.content.Context
import android.util.Log
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.utils.constants.Constants.Companion.ACCESS_TOKEN
import com.ey.hotspot.utils.constants.Constants.Companion.APP_LOGGED_IN
import com.ey.hotspot.utils.constants.Constants.Companion.DELEGATE_ENGLISH_LANG
import com.ey.hotspot.utils.constants.Constants.Companion.LANGUAGE_SELECTED
import com.ey.stringlocalization.utils.LANGUAGE

class MyHotSpotSharedPreference(context: Context) {


    /** set App language Arabic or English */
    fun setLanguage(value: String) {
        LANGUAGE = value
        CoreApp.sharedPreferences.edit().putString(LANGUAGE_SELECTED, value).apply()
    }


    /** get saved App language Arabic or English */
    fun getLanguage(): String? {

        return CoreApp.sharedPreferences.getString(LANGUAGE_SELECTED, DELEGATE_ENGLISH_LANG)
    }

    /*save user access token for api calls*/
    fun saveAccessToken(value: String?) {
        CoreApp.sharedPreferences.edit().putString(ACCESS_TOKEN, value).apply()
    }

    /*Get Access token*/
    fun getAccessToken(): String {
        return CoreApp.sharedPreferences.getString(ACCESS_TOKEN, "") ?: ""
    }


    /*set app logged in status*/
    fun setAppLoggedInStatus(value: Boolean) {
        CoreApp.sharedPreferences.edit().putBoolean(APP_LOGGED_IN, value).apply()
    }

    /*Get app logged in status*/
    fun getAppLoggedInStatus(): Boolean? {
        return CoreApp.sharedPreferences.getBoolean(APP_LOGGED_IN, false)
    }
}