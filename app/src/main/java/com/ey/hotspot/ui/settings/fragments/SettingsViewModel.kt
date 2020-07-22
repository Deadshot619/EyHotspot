package com.ey.hotspot.ui.settings.fragments

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.app_core_lib.HotSpotApp

class SettingsViewModel (application: Application): BaseViewModel(application){

    val currentLanguage = MutableLiveData<String>()

    init {
        currentLanguage.value = if (HotSpotApp.prefs!!.getLanguage() == "en")
            application.getString(R.string.english)
        else
            application.getString(R.string.arabic)
    }

}