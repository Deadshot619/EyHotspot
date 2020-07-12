package com.ey.hotspot.ui.splash_screen

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ActivitySplashBinding
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.utils.constants.Constants
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.logging.Handler

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {


    private val SPLASH_TIME_OUT = 4000
    private var mDelayHandler: Handler? = null

    override fun getLayoutId(): Int = R.layout.activity_splash
    override fun getViewModel(): Class<SplashViewModel> = SplashViewModel::class.java
    override fun onBinding() {
        checkPermission()
        checkappLoginStatus()
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

    fun setUpListeners(){
        mBinding.btArabic.setOnClickListener {

            val langType = HotSpotApp.prefs!!.getLanguage()
            if (langType == Constants.ENGLISH_LANG) {
                HotSpotApp.prefs!!.setLanguage(Constants.ARABIC_LANG)
                restartApplication(this, HotSpotApp.prefs!!)
            }

            startActivity(Intent(this, BottomNavHomeActivity::class.java))
            finish()


        }

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


    private fun checkPermission() {
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
                        //Set Home as initial fragment
                        /*mBinding.bottomNavigation.menu.run {
                            performIdentifierAction(R.id.home, 2)
                            getItem(2).isChecked = true
                        }*/
                    } else if (p0.isAnyPermissionPermanentlyDenied) {
                        Snackbar.make(
                            mBinding.root,
                            "You need to provide Location/GPS permission for this app to run smoothly",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Open") {
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts(
                                            "package",
                                            applicationContext.packageName,
                                            null
                                        )
                                    }
                                this@SplashActivity.startActivity(intent)
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


}