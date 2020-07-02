package com.ey.hotspot.ui.home

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityBottomNavHomeBinding
import com.ey.hotspot.ui.review_and_complaint.ReviewAndComplainFragment
import com.ey.hotspot.ui.speed_test.speed_test_fragmet.SpeedTestFragment

class BottomNavHomeActivity : BaseActivity<ActivityBottomNavHomeBinding, BottomNavHomeViewModel>() {

    override fun getLayoutId() = R.layout.activity_bottom_nav_home
    override fun getViewModel() = BottomNavHomeViewModel::class.java
    override fun onBinding() {

        setBottomNavListener()
    }

    private fun setBottomNavListener(){
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                //Favourites
                R.id.favourite -> {
//                    replaceFragment(MyVisitsFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                //Logs
                R.id.logs -> {
                    replaceFragment(ReviewAndComplainFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                //alert
                R.id.dashboard -> {
//                    replaceFragment(MyVisitsFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                //Speed Test
                R.id.speed_check -> {
                    replaceFragment(SpeedTestFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }

                //Profile
                R.id.profile -> {
//                    replaceFragment(MyVisitsFragment(), false)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
}