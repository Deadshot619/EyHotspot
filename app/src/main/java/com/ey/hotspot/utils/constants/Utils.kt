package com.ey.hotspot.utils.constants

import android.content.Intent
import android.provider.Settings
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.utils.constants.Constants.Companion.ARABIC_LANG
import com.ey.hotspot.utils.constants.Constants.Companion.DL_LINK_2
import com.ey.hotspot.utils.constants.Constants.Companion.ENGLISH_LANG


/**
 * Method to get Device Id
 */
fun getDeviceId() = Settings.Secure.getString(CoreApp.instance.contentResolver, Settings.Secure.ANDROID_ID).toString()

/**
 * returns a deep link url in the form "http://eyhotspot.com?id=69&lat=69.69&lon=69.69"
 */
fun getDeepLinkUrl(id: String) = "$DL_LINK_2$id"

/**
 * This method will check if the Wifi SSID provided has keywords provided by KSA Free Wifi.
 * returns true if it does, else false
 */
fun checkWifiContainsKeywords(wifiSsid: String): Boolean{
    return HotSpotApp.prefs?.getWifiKeywordsPref()?.any { wifiSsid.contains(it, true) } ?: false
}

/**
 * Update shared pref when user Logs in
 */
fun updateSharedPreference(loginResponse: LoginResponse) {
    HotSpotApp.prefs?.run {
        saveAccessToken(loginResponse.accessToken)
        setAppLoggedInStatus(true)
        setSkipStatus(false)
        setUserDataPref(loginResponse)
    }
}

/**
 * Update shared pref when user skips Logs in
 */
fun setSkippedUserData(){
    HotSpotApp.prefs?.run {
        setAppLoggedInStatus(false)
        setSkipStatus(true)
    }
}

/**
 * This method will be used to convert a number of lists of strings into a single string.
 * Will mainly be used in processing validation errors from server as they are received as lists of string
 */
fun convertStringFromList(vararg lists: List<String>?): String{
    var str = ""

    for(i in lists){
        i?.let {
            str += i.joinToString(separator = ",\n")
            str += "\n\n"
        }
    }

    return str
}

/**
 * Method to logout user & go to Login Page
 */
fun logoutUser() {
    clearDataSaveLangAndKeywords()

    //Stop Service
//    CoreApp.instance.stopService(Intent(CoreApp.instance, WifiService::class.java))

    //Redirect user to Login Activity
    CoreApp.instance.startActivity(Intent(CoreApp.instance, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

/**
 * This method will clear all the sharedPref data except Language
 */
fun clearDataSaveLangAndKeywords(){
    val lang = if (ENGLISH_LANG == HotSpotApp.prefs?.getLanguage()) ENGLISH_LANG else ARABIC_LANG
    val temp = HotSpotApp.prefs?.getWifiKeywordsPref()

    //Clear Data
    HotSpotApp.prefs?.clearSharedPrefData()

    //Set language
    HotSpotApp.prefs?.setLanguage(lang)
    temp?.let {
        //Set keywords
        HotSpotApp.prefs?.saveWifiKeywordsPref(it)
    }
}

fun clearDataSaveWifiKeywords(){
    val temp = HotSpotApp.prefs?.getWifiKeywordsPref()


}