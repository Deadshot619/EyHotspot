package com.ey.hotspot.ui.login

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.databinding.ActivityLoginBinding
import com.ey.hotspot.service.WifiService
import com.ey.hotspot.ui.login.login_fragment.LoginFragment
import com.ey.hotspot.ui.registration.webview.WebViewFragment
import com.ey.hotspot.utils.IOnBackPressed
import com.ey.hotspot.utils.LANGUAGE
import com.ey.hotspot.utils.constants.Constants
import java.util.*


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
            (instanceFragment as? IOnBackPressed)?.onBackPressed().let {
                if(it==true) {
                    super.onBackPressed()
                }
                else
                {

                }
            }
        }
        else
        {
            super.onBackPressed()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        //These two lines are important to handle language change edge cases
        setLocale(if (LANGUAGE == Constants.ARABIC_LANG) Locale("ar", "AE") else Locale.ENGLISH)
        WebView(this).destroy()

        super.onCreate(savedInstanceState)
    }


    /*
        This has an important consequence for apps that have multiple languages.
        If your app has WebViews, then those are rendered using the Chrome app.
        Because Chrome is an Android app in itself, running in its own sandboxed process, it will not be bound to the locale set by your app.
        Instead, Chrome will revert to the primary device locale.
        For example, say your app locale is set to ar-AE, while the primary locale of the device is en-US.
        In this case, the locale of the Activity containing a WebView will change from ar-AE to en-US,
        and strings and resources from the corresponding locale folders will be displayed.
        You may see a mish-mash of LTR and RTL strings/resources on those Activitys that have WebViews.
     */
    private fun setLocale(locale: Locale) {
        var context: Context = CoreApp.instance
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        Locale.setDefault(locale)
        configuration.setLocale(locale)
        if (Build.VERSION.SDK_INT >= 25) {
            context = context.applicationContext.createConfigurationContext(configuration)
            context = context.createConfigurationContext(configuration)
        }
        context.resources.updateConfiguration(
            configuration,
            resources.displayMetrics
        )
    }
}