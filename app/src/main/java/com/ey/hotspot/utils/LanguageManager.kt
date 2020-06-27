package com.ey.stringlocalization.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*


object LanguageManager {

    @JvmStatic
    fun setLanguage(
        context: Context,
        languageCode: String?):Context {


        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setSystemLocale(config, locale)

        } else {
            setSystemLocaleLegacy(config, locale)

        }
        context.resources.updateConfiguration(config,context.resources.displayMetrics)
        return context


    }


    private fun setSystemLocaleLegacy(config: Configuration, locale: Locale) {
        config.locale = locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun setSystemLocale(config: Configuration, locale: Locale) {
        config.setLayoutDirection(locale)
        config.setLocale(locale)
    }



}