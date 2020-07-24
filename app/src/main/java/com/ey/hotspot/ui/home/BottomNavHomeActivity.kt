package com.ey.hotspot.ui.home

import android.content.Intent
import androidx.core.content.ContextCompat
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ActivityBottomNavHomeBinding
import com.ey.hotspot.service.WifiService
import com.ey.hotspot.ui.favourite.FavouriteFragment
import com.ey.hotspot.ui.home.fragment.HomeFragment
import com.ey.hotspot.ui.review_and_complaint.ReviewAndComplainFragment
import com.ey.hotspot.ui.settings.fragments.SettingsFragment
import com.ey.hotspot.ui.speed_test.speed_test_fragmet.SpeedTestFragment
import com.ey.hotspot.utils.*
import com.ey.hotspot.utils.constants.logoutUser
import com.ey.hotspot.utils.dialogs.YesNoDialog

class BottomNavHomeActivity : BaseActivity<ActivityBottomNavHomeBinding, BottomNavHomeViewModel>() {

    private val dialog by lazy { YesNoDialog(this).apply {
        //TODO 17/07/2020 : Extract String resources
        setViews(
            title = getString(R.string.login_required),
            description = getString(R.string.need_to_login),
            yes = {
                goToLoginScreen()
                this.dismiss()
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
                        clearFragmentBackstack()
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
                        clearFragmentBackstack()
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
                    clearFragmentBackstack()
                    replaceFragment(HomeFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                //Speed Test
                R.id.speed_check -> {
                    clearFragmentBackstack()
                    replaceFragment(SpeedTestFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                //Profile
                /*R.id.profile -> {
                    clearFragmentBackstack()
                    replaceFragment(ProfileFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }*/

                //Settings
                R.id.settings -> {
                    clearFragmentBackstack()
                    replaceFragment(SettingsFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }



}