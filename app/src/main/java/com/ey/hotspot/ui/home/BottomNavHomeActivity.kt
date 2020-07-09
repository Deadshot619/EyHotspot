package com.ey.hotspot.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityBottomNavHomeBinding
import com.ey.hotspot.service.WifiService
import com.ey.hotspot.ui.favourite.fragment.FavouriteFragment
import com.ey.hotspot.ui.home.fragment.HomeFragment
import com.ey.hotspot.ui.profile.fragment.ProfileFragment
import com.ey.hotspot.ui.review_and_complaint.ReviewAndComplainFragment
import com.ey.hotspot.ui.speed_test.speed_test_fragmet.SpeedTestFragment
import com.ey.hotspot.utils.CHANNEL_ID
import com.ey.hotspot.utils.channel_name
import com.ey.hotspot.utils.createNotificationChannel
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class BottomNavHomeActivity : BaseActivity<ActivityBottomNavHomeBinding, BottomNavHomeViewModel>() {

    override fun getLayoutId() = R.layout.activity_bottom_nav_home
    override fun getViewModel() = BottomNavHomeViewModel::class.java
    override fun onBinding() {

        setBottomNavListener()

        startWifiCheckService()

        //Set Home as initial fragment
        mBinding.bottomNavigation.menu.run{
            performIdentifierAction(R.id.home, 2)
            getItem(2).isChecked = true
        }

        checkPermission()
    }

    private fun startWifiCheckService() {
        //Create Notification channel
        createNotificationChannel(
            this,
            CHANNEL_ID,
            channel_name
        )

        //Start foreground service
        ContextCompat.startForegroundService(this, Intent(this, WifiService::class.java))
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

    private fun checkPermission() {
        Dexter.withContext(this)
            .withPermissions(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                } else {
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                }
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0!!.areAllPermissionsGranted()) {
                        //Set Home as initial fragment
                        /*mBinding.bottomNavigation.menu.run {
                            performIdentifierAction(R.id.home, 2)
                            getItem(2).isChecked = true
                        }*/
                    } else if (p0.isAnyPermissionPermanentlyDenied) {
                        Snackbar.make(
                            mBinding.root,
                            "You need to provide Location/GPS permission for this app to run smoothly",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Open") {
                                val intent =
                                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts(
                                            "package",
                                            applicationContext.packageName,
                                            null
                                        )
                                    }
                                this@BottomNavHomeActivity.startActivity(intent)
                            }
                            .show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

            }).check()
    }
}