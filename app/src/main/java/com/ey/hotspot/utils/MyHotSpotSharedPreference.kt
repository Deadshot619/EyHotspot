package com.ey.hotspot.utils

import android.content.Context
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.Constants.Companion.ACCESS_TOKEN
import com.ey.hotspot.utils.constants.Constants.Companion.APP_LOGGED_IN
import com.ey.hotspot.utils.constants.Constants.Companion.DELEGATE_ENGLISH_LANG
import com.ey.hotspot.utils.constants.Constants.Companion.ENABLED_GPS_LOCATION
import com.ey.hotspot.utils.constants.Constants.Companion.FIRST_TIME_LOGIN_OR_SKIPPED
import com.ey.hotspot.utils.constants.Constants.Companion.FORGOT_PASSWORD_FIELD
import com.ey.hotspot.utils.constants.Constants.Companion.LANGUAGE_SELECTED
import com.ey.hotspot.utils.constants.Constants.Companion.REGISTRATION_EMAIL_ID
import com.ey.hotspot.utils.constants.Constants.Companion.REGISTRATION_TMP_TOKEN
import com.ey.hotspot.utils.constants.Constants.Companion.SKIP_STATUS
import com.ey.hotspot.utils.constants.Constants.Companion.TERMS_AND_CONDITION
import com.ey.hotspot.utils.constants.Constants.Companion.USER_DATA
import com.ey.hotspot.utils.constants.Constants.Companion.WIFI_KEYWORDS
import com.ey.hotspot.utils.extention_functions.fromJson
import com.google.gson.Gson

class MyHotSpotSharedPreference(context: Context) {


    /** set App language Arabic or English */
    fun setLanguage(value: String) {
        LANGUAGE = value
        CoreApp.sharedPreferences.edit().putString(LANGUAGE_SELECTED, value).apply()
    }


    /** get saved App language Arabic or English */
    fun getLanguage(): String {
        return CoreApp.sharedPreferences.getString(LANGUAGE_SELECTED, DELEGATE_ENGLISH_LANG)
            ?: DELEGATE_ENGLISH_LANG
    }

    fun setLanguageFirstTime(value: Boolean){
        CoreApp.sharedPreferences.edit().putBoolean(Constants.LANGUAGE_SELECTED_FIRST_TIME, value).apply()
    }

    fun getLanguageFirstTime(): Boolean = CoreApp.sharedPreferences.getBoolean(Constants.LANGUAGE_SELECTED_FIRST_TIME, false) ?: false

    /*save user access token for api calls*/
    fun saveAccessToken(value: String?) {
        CoreApp.sharedPreferences.edit().putString(ACCESS_TOKEN, value).apply()
    }

    /*Get Access token*/
    fun getAccessToken(): String {
        return getUserDataPref()?.accessToken ?: ""
    }

    /**
     * Returns the access token in the format "TokenType AccessToken" e.g: "Bearer xyz"
     */
    fun getAccessTypeAndToken(): String {
        return "${HotSpotApp.prefs?.getUserDataPref()?.tokenType} ${HotSpotApp.prefs?.getUserDataPref()?.accessToken}"
    }

    /*set app logged in status*/
    fun setAppLoggedInStatus(value: Boolean) {
        CoreApp.sharedPreferences.edit().putBoolean(APP_LOGGED_IN, value).apply()
    }

    /*Get app logged in status*/
    fun getAppLoggedInStatus(): Boolean? {
        return CoreApp.sharedPreferences.getBoolean(APP_LOGGED_IN, false)
    }

    /*
     *  Set this data when user has Skipped Sign In
     */
    fun setSkipStatus(value: Boolean) {
        CoreApp.sharedPreferences.edit().putBoolean(SKIP_STATUS, value).apply()
    }

    //Return status of skipped user, true if skipped, else false
    fun getSkipStatus(): Boolean {
        return CoreApp.sharedPreferences.getBoolean(SKIP_STATUS, false)
    }

    /**
     * Set User Data in SharedPref
     */
    fun setUserDataPref(userData: LoginResponse) {
        CoreApp.sharedPreferences.edit().putString(USER_DATA, Gson().toJson(userData)).apply()
    }


    fun setGPSEnabledStatus(value: Boolean) {
        CoreApp.sharedPreferences.edit().putBoolean(ENABLED_GPS_LOCATION, value).apply()
    }

    fun getGPSEnabledStatus(): Boolean? {
        return CoreApp.sharedPreferences.getBoolean(ENABLED_GPS_LOCATION, false)

    }

    /**
     * Get User Data in SharedPref
     */
    fun getUserDataPref(): LoginResponse? {
        return Gson().fromJson<LoginResponse>(
            CoreApp.sharedPreferences.getString(USER_DATA, "") ?: ""
        )
    }

    fun deleteUserDataPref() {
        CoreApp.sharedPreferences.edit().remove(USER_DATA).apply()
    }


    fun setRegistrationTempToken(tmp: String) {
        CoreApp.sharedPreferences.edit().putString(REGISTRATION_TMP_TOKEN, tmp).apply()
    }

    fun getRegistrationTempToken(): String? {
        return CoreApp.sharedPreferences.getString(REGISTRATION_TMP_TOKEN, "")

    }


    fun setForgotPasswordField(field: String) {
        CoreApp.sharedPreferences.edit().putString(FORGOT_PASSWORD_FIELD, field).apply()
    }

    fun getForgotPasswordField(): String? {
        return CoreApp.sharedPreferences.getString(FORGOT_PASSWORD_FIELD, "")
    }

    /**
     * Method to clear all shared pref data
     */
    fun clearSharedPrefData() {
        CoreApp.sharedPreferences.edit().clear().apply()
    }

    fun setTermsConditionStatus(value: Boolean) {
        CoreApp.sharedPreferences.edit().putBoolean(TERMS_AND_CONDITION, value).apply()

    }

    fun getTermsAndConditionStatus(): Boolean? {
        return CoreApp.sharedPreferences.getBoolean(TERMS_AND_CONDITION, false)

    }

    fun setRegistrationEmailID(emailID: String) {
        CoreApp.sharedPreferences.edit().putString(REGISTRATION_EMAIL_ID, emailID).apply()

    }

    /*
     *  These getter & setter methods will help to determine whether user was logged in for first time or not
     */
    fun setFirstTimeLoginOrSkipped(value: Boolean){
        CoreApp.sharedPreferences.edit().putBoolean(FIRST_TIME_LOGIN_OR_SKIPPED, value).apply()
    }

    fun getFirstTimeLoginOrSkipped(): Boolean{
        return CoreApp.sharedPreferences.getBoolean(FIRST_TIME_LOGIN_OR_SKIPPED, false)
    }

    /*
     *  Method to save & retrieve Wifi Keywords from SharedPref that we get in Splash Activity
     */
    fun saveWifiKeywordsPref(list: List<String>) {
        CoreApp.sharedPreferences.edit().putString(WIFI_KEYWORDS, Gson().toJson(list)).apply()
    }

    fun getWifiKeywordsPref(): List<String>? {
        return Gson().fromJson<List<String>>(
            CoreApp.sharedPreferences.getString(
                WIFI_KEYWORDS,
                null
            ) ?: ""
        )
    }
}