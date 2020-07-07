package com.ey.stringlocalization.utils

interface Constants {

    companion object {
        const val LANGUAGE_SELECTED: String = "SelectedLanguage"
        const val DELEGATE_ENGLISH_LANG = "en"
        const val ENGLISH_LANG: String  = "en"
        const val ARABIC_LANG: String  = "ar"


        //APIS


        //POST
        const val  API_LOGIN ="login"
        const val  API_REGISTRATION ="register"
        const val API_NEARBYWIFILIST=""
        const val API_USER_LIST=""
        const val API_LOGOUT=""
        const val API_REFRESH_TOKEN=""
        const val API_UPDATE_PROFILE=""

        //GET
        const val API_GET_USER_LIST="getProfile"
    }
}