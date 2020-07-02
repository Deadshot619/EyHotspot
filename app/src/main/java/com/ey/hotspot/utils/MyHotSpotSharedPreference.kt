package com.ey.stringlocalization.utils

import android.content.Context
import android.util.Log
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.stringlocalization.utils.Constants.Companion.DELEGATE_ENGLISH_LANG
import com.ey.stringlocalization.utils.Constants.Companion.LANGUAGE_SELECTED

class MyHotSpotSharedPreference (context: Context) {


    /** set App language Arabic or English */
    fun setLanguage(value: String) {
        LANGUAGE = value
        Log.d("Langu set language",value)
        CoreApp.sharedPreferences.edit().putString(LANGUAGE_SELECTED, value).apply()
    }



    /** get saved App language Arabic or English */
    fun getLanguage(): String? {
        Log.d("Langu Get language",CoreApp.sharedPreferences.getString(LANGUAGE_SELECTED, DELEGATE_ENGLISH_LANG))
        return CoreApp.sharedPreferences.getString(LANGUAGE_SELECTED, DELEGATE_ENGLISH_LANG)
    }
}