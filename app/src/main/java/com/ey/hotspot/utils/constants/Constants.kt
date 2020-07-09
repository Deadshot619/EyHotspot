package com.ey.hotspot.utils.constants

interface Constants {

    companion object {
        const val LANGUAGE_SELECTED: String = "SelectedLanguage"
        const val DELEGATE_ENGLISH_LANG = "en"
        const val ENGLISH_LANG: String = "en"
        const val ARABIC_LANG: String = "ar"

        //SharedPreference

        const val  ACCESS_TOKEN:String="access_token"
        const val  APP_LOGGED_IN:String ="app_logged_in"


        //APIS


        //POST
        const val API_LOGIN = "login"
        const val API_REGISTRATION = "register"
        const val API_GET_PROFILE = "getProfile"
        const val API_LOGOUT = "logout"
        const val API_REFRESH_TOKEN = "refreshToken"
        const val API_UPDATE_PROFILE = "updateProfile"

        //GET
        const val API_GET_USER_LIST = "userList"
    }
}