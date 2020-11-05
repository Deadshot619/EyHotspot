package com.ey.hotspot.utils.constants

enum class OptionType(name: String) { TYPE_REGISTRATION("Registration"), TYPE_FORGOT_PASSWORD("ForgotPassword") }

enum class LoadingStatus {
    SUCCESS,
    ERROR,
    LOADING
}

enum class ReviewTypeEnum(val value: String) { ADD_REVIEW("add_review"), EDIT_REVIEW("edit_review") }

enum class VerificationType(val value: String){ EMAIL("email"), SMS("sms") }

enum class ReviewSortType(val value: String){ LATEST("latest"), OLDEST("oldest") }

enum class SpeedTestModes(val value: String){ FOREGROUND("foreground"), BACKGROUND("background") }