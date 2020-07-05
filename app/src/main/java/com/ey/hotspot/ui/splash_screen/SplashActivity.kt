package com.ey.hotspot.ui.splash_screen

import android.content.Intent
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ActivitySplashBinding
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.stringlocalization.utils.Constants

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_splash
    override fun getViewModel(): Class<SplashViewModel> = SplashViewModel::class.java
    override fun onBinding() {


        mBinding.btArabic.setOnClickListener {

            startActivity(Intent(this, BottomNavHomeActivity::class.java))
            finish()

            val langType = HotSpotApp.prefs!!.getLanguage()
            if (langType == Constants.ENGLISH_LANG) {
                HotSpotApp.prefs!!.setLanguage(Constants.ARABIC_LANG)
                restartApplication(this, HotSpotApp.prefs!!)
            }


        }

        mBinding.btEnglish.setOnClickListener {
            startActivity(Intent(this, BottomNavHomeActivity::class.java))
            finish()

            val langType = HotSpotApp.prefs!!.getLanguage()
            if (langType == Constants.ARABIC_LANG) {
                HotSpotApp.prefs!!.setLanguage(Constants.ENGLISH_LANG)
                restartApplication(this, HotSpotApp.prefs!!)
            }

        }

    }

}