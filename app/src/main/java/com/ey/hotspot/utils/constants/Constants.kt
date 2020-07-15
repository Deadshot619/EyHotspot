package com.ey.hotspot.utils.constants

interface Constants {

    companion object {
        const val LANGUAGE_SELECTED: String = "SelectedLanguage"
        const val DELEGATE_ENGLISH_LANG = "en"
        const val ENGLISH_LANG: String = "en"
        const val ARABIC_LANG: String = "ar"

        //Intent
        const val AUTHENTICATION_FAILED = "authentication_failed"   //used when user's authticaition is failed while refreshing token

        //SharedPreference
        const val ACCESS_TOKEN: String = "access_token"
        const val APP_LOGGED_IN: String = "app_logged_in"
        const val USER_DATA = "user_data"

        //APIS

        //Header
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HEADER_X_LOCALIZATION = "X-localization"
        const val HEADER_REFRESH_TOKEN = "Refresh-Token"

        //POST
        const val API_LOGIN = "login"
        const val API_SOCIAL_LOGIN="socialLogin"

        const val API_REGISTRATION = "register"
        const val API_GET_PROFILE = "getProfile"
        const val API_LOGOUT = "logout"
        const val API_REFRESH_TOKEN = "refreshToken"
        const val API_UPDATE_PROFILE = "updateProfile"
        const val API_GET_HOTPSOT = "getHotspots"
        const val API_GET_USER_HOTSPOT_LIST = "getUsersHotspots"
        const val API_MARK_FAVOURITE = "markFavourites"
        const val API_FAVOURITE_LIST = "getFavourites"
        const val API_ADD_REVIEW = "addReview"
        const val API_ADD_COMPLAINT = "addComplaint"

        //GET
        const val UNAUTHORIZED = "unauthorized"
        const val API_GET_USER_LIST = "userList"
        const val API_GET_REVIEWS_AND_COMPLAINTS = "getReviewsAndComplaints"
        const val API_GET_COMPLAINTS_ISSUE_TYPES = "getComplaintIssueTypes"

    }
}