package com.ey.hotspot.ui.splash_screen

import android.content.Intent
import android.view.View
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
import java.util.logging.Handler

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {


    private val SPLASH_TIME_OUT = 4000
    private var mDelayHandler: Handler? = null

    override fun getLayoutId(): Int = R.layout.activity_splash
    override fun getViewModel(): Class<SplashViewModel> = SplashViewModel::class.java
    override fun onBinding() {
        checkLocationPermission(mBinding.root) {
            if (!isLocationEnabled())
                turnOnGpsDialog()
        }

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
}