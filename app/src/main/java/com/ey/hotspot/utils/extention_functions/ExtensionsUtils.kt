package com.ey.hotspot.utils.extention_functions

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.clearDataSaveLangAndKeywords
import com.ey.hotspot.utils.constants.getDeepLinkUrl
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.random.Random


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
fun Context.isLocationEnabled(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
// This is new method provided in API 28
        val lm =
            applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        lm != null && lm.isLocationEnabled
    } else {
// This is Deprecated in API 28
        val mode: Int = Settings.Secure.getInt(
            this.contentResolver, Settings.Secure.LOCATION_MODE,
            Settings.Secure.LOCATION_MODE_OFF
        )
        mode != Settings.Secure.LOCATION_MODE_OFF
    }
}

//Check location status in Xioami phones
fun Context.checkLocSaveState(): Boolean {
    var status: Boolean = false
    val lm = getSystemService(LOCATION_SERVICE) as LocationManager
    var gps_enabled = false
    var network_enabled = false

    try {
        gps_enabled = lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    try {
        network_enabled = lm != null && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    if (gps_enabled && network_enabled) {
        status = true
    } else if (gps_enabled && network_enabled) {
        status = false
    } else if (gps_enabled && network_enabled) {
        status = false
    } else if (gps_enabled && network_enabled) {
        status = false
    }

    return status
}

/**
 * Method to check if Location Permission is permitted
 *
 * @param view The view from which it is called. Used to show SnackBar on
 * @param func The function to be executed when permission is granted
 */
lateinit var mGoogleSignInClient: GoogleSignInClient

fun Activity.checkLocationPermission(view: View, func: (Unit) -> Unit) {
    Dexter.withContext(applicationContext)
        .withPermissions(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                Manifest.permission.ACCESS_COARSE_LOCATION
                android.Manifest.permission.ACCESS_FINE_LOCATION
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
                } else if (p0.deniedPermissionResponses.isNotEmpty()) {
                    showMessage(getString(R.string.provide_location_permission_label), true)
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
        .setMessage(getString(R.string.gps_enable_conformation))
        .setCancelable(false)
        .setPositiveButton(R.string.yes_label) { dialog, id ->
            startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        .setNegativeButton(getString(R.string.no_label)) { dialog, id ->
            dialog.cancel()
        }
        .show()
}

fun Activity.turnGPSOn() {
    val provider = Settings.Secure.getString(
        contentResolver,
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
        val info: PackageInfo = getPackageManager()
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

fun Activity.goToLoginScreen() {
    startActivity(Intent(this, LoginActivity::class.java))
}

fun Activity.goToHomeScreen() {
    startActivity(Intent(this, BottomNavHomeActivity::class.java).apply {
        flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
    })
    this.finish()
}

fun Activity.generateCaptchaCode(limit: Int): String? {
    val chars = resources.getString(R.string.captcha_characters)
    val buf = StringBuffer()
    buf.append(chars[Random.nextInt(10)])
    for (i in 0 until limit) {
        buf.append(chars[Random.nextInt(chars.length)])
    }
    return buf.toString()
}


fun Context.getUserLocation(func: (lat: Double?, lon: Double?) -> Unit) {
    val client = FusedLocationProviderClient(this)

    try {
        if (checkLocSaveState())
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val location = client.lastLocation
                location.addOnCompleteListener {
                    func(it.result?.latitude, it.result?.longitude)
                    Timber.tag("Location Complete").i("${it.result?.latitude} ${it.result?.longitude}")
//                    if (it.isSuccessful) { }
                }
                location.addOnFailureListener {
                    func(null, null)
                    Timber.tag("Location Failed").i("null null")

                }
            }
    } catch (e: Exception) {
        Timber.tag("Location Exception").i("null null")
    }
}

/**
 * This method will be used to share Wifi Hotspot data to other apps as text message
 */
fun Activity.shareWifiHotspotData(hotspotName: String, operatorName: String, city: String, id: String) {
//    val data = "Get free access to our public Wi-Fi at $hotspotName, by $operatorName, in $city \n ${getDeepLinkUrl(id = id)}"
    val data = "${String.format(getString(R.string.share_hotspot_data), hotspotName, operatorName, city)} \n ${getDeepLinkUrl(id = id)}"

    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, data)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
//    showMessage(getString(R.string.under_construction_label))
}

/**
 * This method will show possible apps that can open the link
 */
fun Activity.openNavigateUrl(url: String, lat: String, lon: String) {
    val gmmIntentUri =
        Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$lat,$lon&mode=d")
//        Uri.parse(url)
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                mapIntent.setPackage("com.google.android.apps.maps")
    if (mapIntent.resolveActivity(packageManager) != null)
        startActivity(Intent.createChooser(mapIntent, null))
    else
        showMessage("There are no apps to open this link")
}

/**
 * Method to logout user & go to Login Page
 */
fun Application.logoutUser(bundle: Bundle? = null) {
    clearDataSaveLangAndKeywords()

    //Stop Service
//    CoreApp.instance.stopService(Intent(CoreApp.instance, WifiService::class.java))

    //Redirect user to Login Activity
    this.startActivity(Intent(this, LoginActivity::class.java).apply {
        flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        putExtra(Constants.LOGIN_BUNDLE, bundle)
    })
}
