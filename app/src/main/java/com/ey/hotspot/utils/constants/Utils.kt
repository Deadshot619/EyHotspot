package com.ey.hotspot.utils.constants

import android.content.Intent
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.login.LoginActivity


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
 * Method to logout user
 */
fun goToLoginScreen() {
    //Clear Data
    HotSpotApp.prefs?.clearSharedPrefData()

    //Redirect user to Login Activity
    CoreApp.instance.startActivity(Intent(CoreApp.instance, LoginActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    })
}
