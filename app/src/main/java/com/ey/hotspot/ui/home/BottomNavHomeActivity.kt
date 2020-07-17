package com.ey.hotspot.ui.home

import android.content.Intent
import androidx.core.content.ContextCompat
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ActivityBottomNavHomeBinding
import com.ey.hotspot.service.WifiService
import com.ey.hotspot.ui.favourite.FavouriteFragment
import com.ey.hotspot.ui.home.fragment.HomeFragment
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.ui.profile.ProfileFragment
import com.ey.hotspot.ui.review_and_complaint.ReviewAndComplainFragment
import com.ey.hotspot.ui.speed_test.speed_test_fragmet.SpeedTestFragment
import com.ey.hotspot.utils.CHANNEL_ID
import com.ey.hotspot.utils.channel_name
import com.ey.hotspot.utils.createNotificationChannel
import com.ey.hotspot.utils.dialogs.YesNoDialog
import com.ey.hotspot.utils.wifi_notification_key

class BottomNavHomeActivity : BaseActivity<ActivityBottomNavHomeBinding, BottomNavHomeViewModel>() {

    private val dialog by lazy { YesNoDialog(this).apply {
        //TODO 17/07/2020 : Extract String resources
        setViews(
            title = "Login Required",
            description = "You need to login to access this feature",
            yes = {
                goToLoginScreen()
            },
            no = {
                this.dismiss()
            })
    } }

    override fun getLayoutId() = R.layout.activity_bottom_nav_home
    override fun getViewModel() = BottomNavHomeViewModel::class.java
    override fun onBinding() {

        setBottomNavListener()

        //Start service only if it isn't running already
        if (!WifiService.isRunning)
            startWifiCheckService()

        //Set Home as initial fragment
        mBinding.bottomNavigation.menu.run {
            performIdentifierAction(R.id.home, 2)
            getItem(2).isChecked = true
        }


//        showMessage(HotSpotApp.prefs?.getUserDataPref()?.accessToken ?: "")
    }

    private fun startWifiCheckService() {
        //Create Notification channel
        createNotificationChannel(
            this,
            CHANNEL_ID,
            channel_name
        )

        //Start foreground service
        ContextCompat.startForegroundService(this, Intent(this, WifiService::class.java).apply {
            putExtra(wifi_notification_key, getString(R.string.checking_wifi_connection_label))
        })
    }

    private fun setBottomNavListener() {
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                //Favourites
                R.id.favourite -> {
                    if (HotSpotApp.prefs?.getAppLoggedInStatus()!!){
                        replaceFragment(FavouriteFragment(), false)
                        return@setOnNavigationItemSelectedListener true
                    }else{
//                        showMessage("Login to continue")
                        dialog.show()
                        return@setOnNavigationItemSelectedListener false
                    }
                }

                //Reviews & complaints
                R.id.logs -> {
                    if (HotSpotApp.prefs?.getAppLoggedInStatus()!!){
                        replaceFragment(ReviewAndComplainFragment(), false)
                        return@setOnNavigationItemSelectedListener true
                    }else{
//                        showMessage("Login to continue")
                        dialog.show()
                        return@setOnNavigationItemSelectedListener false
                    }
                }

                //home
                R.id.home -> {
                    replaceFragment(HomeFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                //Speed Test
                R.id.speed_check -> {
                    replaceFragment(SpeedTestFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                //Profile
                R.id.profile -> {
                    replaceFragment(ProfileFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun goToLoginScreen(){
        //Clear Data
        HotSpotApp.prefs?.clearSharedPrefData()

        //Redirect user to Login Activity
        CoreApp.instance.startActivity(Intent(CoreApp.instance, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

}