package com.ey.hotspot.ui.deep_link

import android.content.Intent
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ActivityDeepLinkBinding
import com.ey.hotspot.ui.deep_link.model.DeepLinkHotspotDataModel
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.utils.constants.Constants.Companion.DL_DATA

class DeepLinkActivity : BaseActivity<ActivityDeepLinkBinding, DeepLinkViewModel>() {
    lateinit var deepLinkData: DeepLinkHotspotDataModel

    override fun getLayoutId(): Int = R.layout.activity_deep_link
    override fun getViewModel() = DeepLinkViewModel::class.java
    override fun onBinding() {
        CoreApp.DL_START = 0

        deepLinkData = DeepLinkHotspotDataModel(
            uuid = intent.dataString.toString().substringAfter("dashboard/")
        )

        checkAppLoginStatus()
    }

    private fun checkAppLoginStatus() {
        when {
            HotSpotApp.prefs?.getAppLoggedInStatus()!! -> { //If user is already logged in
                goToHomePage()
            }
            HotSpotApp.prefs?.getSkipStatus()!! -> {    //If user has skipped login
                goToHomePage()
            }
            else -> {   //Stay on splash screen & show select lang dialogue
                goToLoginPage()
            }
        }
    }

    //Method to redirect user to homepage
    private fun goToHomePage() {
        startActivity(Intent(this, BottomNavHomeActivity::class.java).apply {
            putExtra(DL_DATA, deepLinkData)
        })
        finish()
    }

    //Method to redirect user to homepage
    private fun goToLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}