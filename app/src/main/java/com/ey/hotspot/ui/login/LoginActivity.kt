package com.ey.hotspot.ui.login

import android.content.Intent
import androidx.fragment.app.Fragment
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityLoginBinding
import com.ey.hotspot.service.WifiService
import com.ey.hotspot.ui.login.login_fragment.LoginFragment
import com.ey.hotspot.ui.registration.webview.WebViewFragment
import com.ey.hotspot.ui.speed_test.rate_wifi.RateWifiFragment
import com.ey.hotspot.utils.IOnBackPressed
import com.ey.hotspot.utils.constants.Constants

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override fun getLayoutId(): Int {

        return R.layout.activity_login
    }

    override fun getViewModel(): Class<LoginViewModel> {

        return LoginViewModel::class.java
    }

    override fun onBinding() {

        supportActionBar?.title = getString(R.string.login_button)
//        calculateHashKey("com.ey.hotspot.ey_hotspot")

        //If service is running, stop the service
        if (WifiService.isRunning)
            stopService(Intent(this, WifiService::class.java))


        replaceFragment(
            fragment = LoginFragment.newInstance(
                goToVerificationFragment = intent?.getBundleExtra(Constants.LOGIN_BUNDLE)
                    ?.getBoolean(Constants.GO_TO_VERIFICATION_FRAGMENT) ?: false,
                email = intent?.getBundleExtra(Constants.LOGIN_BUNDLE)
                    ?.getString(Constants.EMAIL_ID),
                tempToken = intent?.getBundleExtra(Constants.LOGIN_BUNDLE)
                    ?.getString(Constants.TEMP_TOKEN)
            ), addToBackstack = false, bundle = null
        )
    }

    override fun onBackPressed() {

        val instanceFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.container)
        if (instanceFragment is WebViewFragment) {
            (instanceFragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
                super.onBackPressed()
            }
        }
        else
        {
            super.onBackPressed()
        }



    }
}