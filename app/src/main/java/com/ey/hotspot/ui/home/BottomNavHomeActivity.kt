package com.ey.hotspot.ui.home

import android.content.Intent
import androidx.core.content.ContextCompat
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityBottomNavHomeBinding
import com.ey.hotspot.service.WifiService
import com.ey.hotspot.ui.favourite.FavouriteFragment
import com.ey.hotspot.ui.home.fragment.HomeFragment
import com.ey.hotspot.ui.profile.ProfileFragment
import com.ey.hotspot.ui.review_and_complaint.ReviewAndComplainFragment
import com.ey.hotspot.ui.speed_test.speed_test_fragmet.SpeedTestFragment
import com.ey.hotspot.utils.CHANNEL_ID
import com.ey.hotspot.utils.channel_name
import com.ey.hotspot.utils.createNotificationChannel
import com.ey.hotspot.utils.wifi_notification_key

class BottomNavHomeActivity : BaseActivity<ActivityBottomNavHomeBinding, BottomNavHomeViewModel>() {

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
                    replaceFragment(FavouriteFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                //Logs
                R.id.logs -> {
                    replaceFragment(ReviewAndComplainFragment(), false)
                    return@setOnNavigationItemSelectedListener true
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


}