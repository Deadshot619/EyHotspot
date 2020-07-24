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
fun String.isValidPassword(): Boolean {
    return if(this.length >= 8){
        //Contains small letter, Capital letter, a number
        val textPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")
        //Contains special character
        val special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]")
        textPattern.matcher(this).matches() && special.matcher(this).find()
    } else {
        false
    }
}

/**
 * Method to Check if Mobile Number is valid
 */
fun String.isValidMobile(): Boolean{
    return Pattern.matches("[0-9]+", this) && this.length in 10..10
}