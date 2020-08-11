package com.ey.hotspot.utils.constants

interface Constants {

    companion object {
        const val LANGUAGE_SELECTED: String = "SelectedLanguage"
        const val DELEGATE_ENGLISH_LANG = "en"
        const val ENGLISH_LANG: String = "en"
        const val ARABIC_LANG: String = "ar"

        //Default Location Lat Lng of Saudi Arabia
        const val LATITUDE: Double = 23.8859
        const val LONGITUDE: Double = 45.0792

        //Deep Link
        const val DL_LINK = "http://eyhotspot.com"
        const val DL_ID = "id"
        const val DL_LAT = "lat"
        const val DL_LON = "lon"
        const val DL_DATA = "deep_link_hotspot_data"


        //Intent
        const val AUTHENTICATION_FAILED = "authentication_failed"   //used when user's authentication is failed while refreshing token
        const val LOGIN_BUNDLE = "login_bundle"
        const val GO_TO_VERIFICATION_FRAGMENT = "go_to_verification_fragment"
        const val EMAIL_ID = "email_id"
        const val TEMP_TOKEN = "temp_token"


        //Download Link
        const val DOWNLOAD_LINK =
            "https://images.apple.com/v/imac-with-retina/a/images/overview/5k_image.jpg"

        //Country Code
        const val SAUDI_ARABIA_COUNTRY_CODE = /*966*/ 187

        const val UNKNOWN_SSID = "unknown ssid"

        //SharedPreference
        const val ACCESS_TOKEN: String = "access_token"
        const val APP_LOGGED_IN: String = "app_logged_in"
        const val USER_DATA = "user_data"
        const val ENABLED_GPS_LOCATION: String = "GPS_LOCATION"
        const val REGISTRATION_TMP_TOKEN = "registration_token"
        const val SKIP_STATUS = "skip_status"
        const val VERIFY_FORGOT_PASSWORD="verify_forgot_password"
        const val FORGOT_PASSWORD_FIELD="forgot_password_field"
        const val  TERMS_AND_CONDITION="termscondition"
        const val  REGISTRATION_EMAIL_ID="REGISTRATION_EMAIL_ID"
        const val WIFI_KEYWORDS = "wifi_keywords"

        //APIS

        //Header
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HEADER_X_LOCALIZATION = "X-localization"
        const val HEADER_REFRESH_TOKEN = "Refresh-Token"

        /*    POST    */
        const val API_LOGIN = "login"
        const val API_SOCIAL_LOGIN = "socialLogin"

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
        const val API_SEND_OTP = "sendOTP"
        const val API_VERIFY_OTP = "verifyOTP"
        const val API_FORGOT_PASSWORD = "forgotPassword"
        const val API_FORGOT_PASSWORD_VERIFY_OTP = "forgotPassword/verifyOtp"
        const val API_RESET_PASSWORD = "resetPassword"
        const val API_RESEND_FORGOT_PASSWORD_OTP = "forgotPassword/resendOtp"
        const val API_LOCATION_REVIEWS = "getLocationReviews"
        const val API_TERMNCONDITION="acceptTermsConditions"
        const val TANDC_UR_ENGLISH="https://nearbyhs.php-dev.in/EyHotspots/public/acceptTermsConditions"
        const val TANDC_UR_ARABIC="https://nearbyhs.php-dev.in/EyHotspots/public/acceptTermsConditions/ar"

        //Wifi
        const val API_WIFI_LOGIN = "wifi/login"
        const val API_WIFI_LOGOUT = "wifi/logout"
        const val API_VALIDATE_WIFI = "wifi/validateWifi"
        const val API_MATCH_WIFI = "wifi/matchWifi"
        const val API_WIFI_LOGS= "wifi/userLogs"


        /*    GET    */
        const val UNAUTHORIZED = "unauthorized"
        const val API_GET_USER_LIST = "userList"
        const val API_GET_REVIEWS = "getReviews"
        const val API_GET_COMPLAINTS = "getComplaints"
        const val API_GET_COMPLAINTS_ISSUE_TYPES = "getComplaintIssueTypes"
        const val API_GET_COUNTRY_CODE_NAME = "getCountryCodeList"
        const val API_GET_WIFI_SEARCH_KEY_WORDS = "wifi/wifiSearchKeywords"

        /*  Query   */
        const val REVIEW_ORDER = "review_order"
        const val COMPLAINT_ORDER = "complaint_order"


    }
}