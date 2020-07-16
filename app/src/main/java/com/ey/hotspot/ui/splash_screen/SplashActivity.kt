package com.ey.hotspot.ui.splash_screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ActivitySplashBinding
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.utils.checkLocationPermission
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.isLocationEnabled
import com.ey.hotspot.utils.turnOnGpsDialog
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import java.util.logging.Handler


class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(),
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {


    var mLocationRequest: LocationRequest? = null
    lateinit var mGoogleApiClient: GoogleApiClient
    var result: PendingResult<LocationSettingsResult>? = null
    val REQUEST_LOCATION = 199

    private val SPLASH_TIME_OUT = 4000

    override fun getLayoutId(): Int = R.layout.activity_splash
    override fun getViewModel(): Class<SplashViewModel> = SplashViewModel::class.java
    override fun onBinding() {


        checkLocationPermission(mBinding.root) {
            /* if (!isLocationEnabled())
             turnOnGpsDialog()*/
        }
        setUpGoogleAPIClient()
        checkappLoginStatus()


    }

    private fun setUpGoogleAPIClient() {

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()
        mGoogleApiClient.connect()
    }


    private fun checkappLoginStatus() {

        if (HotSpotApp.prefs!!.getAppLoggedInStatus() == true) {
            mBinding.mbLanguageSelection.visibility = View.GONE

            startActivity(Intent(this, BottomNavHomeActivity::class.java))
            finish()

        } else {

            mBinding.mbLanguageSelection.visibility = View.VISIBLE
            setUpListeners()

        }
    }


    fun setUpListeners() {
        //Arabic
        mBinding.btArabic.setOnClickListener {

            val langType = HotSpotApp.prefs!!.getLanguage()
            if (langType == Constants.ENGLISH_LANG) {
                HotSpotApp.prefs!!.setLanguage(Constants.ARABIC_LANG)
                restartApplication(this, HotSpotApp.prefs!!)
            }

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        //English
        mBinding.btEnglish.setOnClickListener {

            val langType = HotSpotApp.prefs!!.getLanguage()
            if (langType == Constants.ARABIC_LANG) {
                HotSpotApp.prefs!!.setLanguage(Constants.ENGLISH_LANG)
                restartApplication(this, HotSpotApp.prefs!!)
            }

            startActivity(Intent(this, LoginActivity::class.java))
            finish()


        }
    }


    override fun onConnected(p0: Bundle?) {

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.interval = 30 * 1000.toLong()
        mLocationRequest!!.fastestInterval = 5 * 1000.toLong()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)
        builder.setAlwaysShow(true)

        result =
            LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build())
        result!!.setResultCallback { result ->
            val status: Status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    // Do something
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    try {

                        status.startResolutionForResult(this, REQUEST_LOCATION)
                    } catch (e: SendIntentException) {
                    }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    // Do something
                }
            }
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("onActivityResult()", Integer.toString(resultCode))
        when (requestCode) {
            REQUEST_LOCATION -> when (resultCode) {
                Activity.RESULT_OK -> {

                    Log.d("location", "Location enabled")

                    HotSpotApp.prefs!!.setGPSEnabledStatus(true)
                }
                Activity.RESULT_CANCELED -> {

                    Log.d("location", "Location not enabled, user canceled")
                    HotSpotApp.prefs!!.setGPSEnabledStatus(false)

                }
                else -> {
                }
            }
        }
    }


}