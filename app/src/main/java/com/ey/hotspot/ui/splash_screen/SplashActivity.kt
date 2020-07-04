package com.ey.hotspot.ui.splash_screen

import android.content.Intent
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivitySplashBinding
import com.ey.hotspot.ui.login.LoginActivity

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_splash
    override fun getViewModel(): Class<SplashViewModel> = SplashViewModel::class.java
    override fun onBinding() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}