package com.ey.hotspot.utils.constants

enum class OptionType(name: String) { TYPE_REGISTRATION("Registration"), TYPE_FORGOT_PASSWORD("ForgotPassword") }

enum class LoadingStatus {
    SUCCESS,
    ERROR,
    LOADING
}

enum class VerificationType(value: String){ EMAIL("email"), SMS("sms") }
