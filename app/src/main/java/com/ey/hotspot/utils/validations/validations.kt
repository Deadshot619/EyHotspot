package com.ey.hotspot.utils.validations

import java.util.regex.Pattern

/**
 * Method to check if email is valid
 */
fun String.isEmailValid(): Boolean{
    return this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Method to Check if password is valid
 */
fun String.isPasswordValid(confirmPassword: String?): Boolean {
    return this == confirmPassword
}

/**
 * Method to Check if Mobile Number is valid
 */
fun String.isValidMobile(): Boolean{
    return Pattern.matches("[0-9]+", this) && this.length in 10..10
}