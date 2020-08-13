package com.ey.hotspot.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ActivityBottomNavHomeBinding
import com.ey.hotspot.service.WifiService
import com.ey.hotspot.ui.deep_link.model.DeepLinkHotspotDataModel
import com.ey.hotspot.ui.favourite.FavouriteFragment
import com.ey.hotspot.ui.home.fragment.HomeFragment
import com.ey.hotspot.ui.review_and_complaint.ReviewAndComplainFragment
import com.ey.hotspot.ui.settings.fragments.SettingsFragment
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.ui.speed_test.rate_wifi.RateWifiFragment
import com.ey.hotspot.ui.speed_test.speed_test_fragmet.SpeedTestFragment
import com.ey.hotspot.utils.CHANNEL_ID
import com.ey.hotspot.utils.channel_name
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.createNotificationChannel
import com.ey.hotspot.utils.dialogs.YesNoDialog
import com.ey.hotspot.utils.extention_functions.goToLoginScreen
import com.ey.hotspot.utils.extention_functions.showMessage
import com.ey.hotspot.utils.wifi_notification_key


class BottomNavHomeActivity : BaseActivity<ActivityBottomNavHomeBinding, BottomNavHomeViewModel>() {

    private val dialog by lazy {
        YesNoDialog(this).apply {
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
        }
    }

    //dialog for review
    private val dialogReview by lazy {
        YesNoDialog(this).apply {
            setViews(
                title = getString(R.string.discard_review),
                description = "",
                yes = {
                    super.onBackPressed()
                    this.dismiss()
                },
                no = { this.dismiss() }
            )
        }
    }

    //dialog for complaint
    private val dialogComplaint by lazy {
        YesNoDialog(this).apply {
            setViews(
                title = getString(R.string.discard_complaint),
                description = "",
                yes = {
                    super.onBackPressed()
                    this.dismiss()
                },
                no = { this.dismiss() }
            )
        }
    }


    //Variable to hold deep link data
    var dlData: DeepLinkHotspotDataModel? = null

    private var doubleBackToExitPressedOnce = false

    override fun getLayoutId() = R.layout.activity_bottom_nav_home
    override fun getViewModel() = BottomNavHomeViewModel::class.java
    override fun onBinding() {
        //Get deep link data
        dlData = intent.extras?.getParcelable(Constants.DL_DATA)


        setBottomNavListener()

        //Start service only if it isn't running already
        if (!WifiService.isRunning)
        //TODO 25/07/2020 : Uncomment this later
//            startWifiCheckService()

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
                    if (HotSpotApp.prefs?.getAppLoggedInStatus()!!) {
                        clearFragmentBackstack()
                        replaceFragment(FavouriteFragment(), false)
                        return@setOnNavigationItemSelectedListener true
                    } else {
//                        showMessage("Login to continue")
                        dialog.show()
                        return@setOnNavigationItemSelectedListener false
                    }
                }

                //Reviews & complaints
                R.id.logs -> {
                    if (HotSpotApp.prefs?.getAppLoggedInStatus()!!) {
                        clearFragmentBackstack()
                        replaceFragment(ReviewAndComplainFragment(), false)
                        return@setOnNavigationItemSelectedListener true
                    } else {
//                        showMessage("Login to continue")
                        dialog.show()
                        return@setOnNavigationItemSelectedListener false
                    }
                }

                //home
                R.id.home -> {
                    clearFragmentBackstack()
                    replaceFragment(HomeFragment.getInstance(data = dlData), false)
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.run {
            //Set Home as initial fragment
            mBinding.bottomNavigation.menu.run {
//                performIdentifierAction(R.id.home, 2)
                getItem(2).isChecked = true
            }
        }
    }

    override fun onBackPressed() {
        val instanceFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.container)
        if (instanceFragment is RateWifiFragment) {
            dialogReview.show()

        } else if (instanceFragment is RaiseComplaintFragment) {
            dialogComplaint.show()
        } else if (mManager.backStackEntryCount == 0) {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }

            this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
            showMessage(getString(R.string.press_back_again_label))

            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
        } else
            super.onBackPressed()


    }


}