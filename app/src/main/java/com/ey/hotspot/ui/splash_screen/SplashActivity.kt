package com.ey.hotspot.ui.splash_screen

import android.content.Intent
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivitySplashBinding
import com.ey.hotspot.ui.home.BottomNavHomeActivity

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_splash
    override fun getViewModel(): Class<SplashViewModel> = SplashViewModel::class.java
    override fun onBinding() {


        mBinding.btArabic.setOnClickListener {

            startActivity(Intent(this, BottomNavHomeActivity::class.java))
            finish()
        }

        mBinding.btEnglish.setOnClickListener {
            startActivity(Intent(this, BottomNavHomeActivity::class.java))
            finish()

        }

    }

}