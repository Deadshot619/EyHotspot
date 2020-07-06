package com.ey.hotspot.ui.home

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityBottomNavHomeBinding
import com.ey.hotspot.ui.favourite.fragment.FavouriteFragment
import com.ey.hotspot.ui.home.fragment.HomeFragment
import com.ey.hotspot.ui.profile.fragment.ProfileFragment
import com.ey.hotspot.ui.review_and_complaint.ReviewAndComplainFragment
import com.ey.hotspot.ui.settings.fragments.SettingsFragment
import com.ey.hotspot.ui.speed_test.test_result.TestResultsFragment

class BottomNavHomeActivity : BaseActivity<ActivityBottomNavHomeBinding, BottomNavHomeViewModel>() {

    override fun getLayoutId() = R.layout.activity_bottom_nav_home
    override fun getViewModel() = BottomNavHomeViewModel::class.java
    override fun onBinding() {

        replaceFragment(HomeFragment(), false)
        setBottomNavListener()
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
                    replaceFragment(TestResultsFragment(), false)
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