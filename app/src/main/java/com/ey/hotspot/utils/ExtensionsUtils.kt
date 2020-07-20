package com.ey.hotspot.utils

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.media.tv.TvContract.Programs.Genres.encode
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Base64.encode
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.URLEncoder.encode
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import  android.util.Base64


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

/**
 * Method to check if GPS is Enabled/Disabled
 *
 * @return returns true if location is enabled, else false
 */
fun Activity.isLocationEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
        val lm =
            this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        lm !=null && lm.isLocationEnabled
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

/**
 * Method to check if Location Permission is permitted
 *
 * @param view The view from which it is called. Used to show SnackBar on
 * @param func The function to be executed when permission is granted
 */
fun Activity.checkLocationPermission(view: View, func: (Unit) -> Unit) {
    Dexter.withContext(this)
        .withPermissions(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                android.Manifest.permission.ACCESS_FINE_LOCATION
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            } else {
                android.Manifest.permission.ACCESS_FINE_LOCATION
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            }
        )
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                if (p0!!.areAllPermissionsGranted()) {
                    //run function
                    func(Unit)
                } else if (p0.isAnyPermissionPermanentlyDenied) {
                    Snackbar.make(
                        view,
                        getString(R.string.provide_location_permission_label),
                        Snackbar.LENGTH_LONG
                    )
                        .setAction(getString(R.string.open_label)) {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts(
                                        "package",
                                        applicationContext.packageName,
                                        null
                                    )
                                }
                            applicationContext.startActivity(intent)
                        }
                        .show()
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }).check()
}

/**
 * Method to show dialog to turn on the GPS
 */
fun Activity.turnOnGpsDialog() {
    AlertDialog.Builder(this)
        .setMessage("Your GPS seems to be disabled, do you want to enable it?")
        .setCancelable(false)
        .setPositiveButton(R.string.yes_label) { dialog, id ->
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        .setNegativeButton(getString(R.string.no_label)) { dialog, id ->
            dialog.cancel()
        }
        .show()
}




/**
 * This method will a json string as input & return an object of specified type
 */
inline fun <reified T> Gson.fromJson(json: String) = try{
    fromJson<T>(json, object: TypeToken<T>() {}.type)
}catch (e: java.lang.Exception){
    null
}


  fun Activity.turnGPSOn() {
    val provider = Settings.Secure.getString(
        getContentResolver(),
        Settings.Secure.LOCATION_PROVIDERS_ALLOWED
    )
    if (!provider.contains("gps")) { //if gps is disabled
        val poke = Intent()
        poke.setClassName(
            "com.android.settings",
            "com.android.settings.widget.SettingsAppWidgetProvider"
        )
        poke.addCategory(Intent.CATEGORY_ALTERNATIVE)
        poke.data = Uri.parse("3")
        sendBroadcast(poke)
    }




}

 fun Activity.calculateHashKey(yourPackageName: String) {
     try {
         val info: PackageInfo =getPackageManager()
             .getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES)
         for (signature in info.signatures) {
             val md = MessageDigest.getInstance("SHA")
             md.update(signature.toByteArray())
             val hashKey = String(Base64.encode(md.digest(), 0))
             Log.i("HASHKEY", "printHashKey() Hash Key: $hashKey")
         }
     } catch (e: NoSuchAlgorithmException) {
         Log.e("HASHKEY", "printHashKey()", e)
     } catch (e: java.lang.Exception) {
         Log.e("HASHKEY", "printHashKey()", e)
     }
}
