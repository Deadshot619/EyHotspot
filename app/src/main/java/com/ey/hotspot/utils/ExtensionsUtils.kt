package com.ey.hotspot.utils

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ey.hotspot.app_core_lib.BaseActivity
import java.math.BigDecimal
import java.math.RoundingMode


var toast: Toast? = null

fun Activity.showMessage(message: String, lengthLong: Boolean = false) {
    if (toast != null) {
        toast?.cancel()
    }
    toast = Toast.makeText(this, message, if (lengthLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    toast?.show()
}

fun Fragment.showMessage(message: String, lengthLong: Boolean = false) {
    if (toast != null) {
        toast?.cancel()
    }
    toast = Toast.makeText(
        this.activity,
        message,
        if (lengthLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    )
    toast?.show()
}


fun Fragment.addFragment(fragment: Fragment, addToBackStack: Boolean, bundle: Bundle? = null) {
    val activity = this.activity as BaseActivity<*, *>
    activity.addFragment(fragment, addToBackStack, bundle)
}

//Method to replace fragment in the container
fun Fragment.replaceFragment(fragment: Fragment, addToBackStack: Boolean, bundle: Bundle? = null) {
    val activity = this.activity as BaseActivity<*, *>
    activity.replaceFragment(fragment, addToBackStack, bundle)
}

//Method to remove fragment from backstack
fun Fragment.removeFragment(fragment: Fragment) {
    val activity = this.activity as BaseActivity<*, *>
    activity.removeFragment(fragment)
}

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view?.let {
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun Activity.showKeyboard() {
    val view = this.currentFocus
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view?.let {
        imm.showSoftInput(view, 0)
    }
}

fun Activity.isLocationEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
        val lm =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        lm.isLocationEnabled
    } else {
// This is Deprecated in API 28
        val mode: Int = Settings.Secure.getInt(
            this.contentResolver, Settings.Secure.LOCATION_MODE,
            Settings.Secure.LOCATION_MODE_OFF
        )
        mode != Settings.Secure.LOCATION_MODE_OFF
    }
}

/**
 * This method will convert a value of Bits per second to Megabytes per second
 */
fun BigDecimal.convertBpsToMbps(): BigDecimal {
    return try {
        (this / 104857.toBigDecimal()).setScale(
            2,
            RoundingMode.CEILING
        )
    } catch (e: Exception) {
        BigDecimal.valueOf(0)
    }
}



