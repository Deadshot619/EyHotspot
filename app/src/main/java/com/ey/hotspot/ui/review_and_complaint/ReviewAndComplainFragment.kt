package com.ey.hotspot.ui.review_and_complaint

import androidx.viewpager2.widget.ViewPager2
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentReviewAndComplainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ReviewAndComplainFragment : BaseFragment<FragmentReviewAndComplainBinding, ReviewAndComplainViewModel>() {

    override fun getLayoutId() = R.layout.fragment_review_and_complain
    override fun getViewModel() = ReviewAndComplainViewModel::class.java

    private lateinit var mAdapter : ReviewAndComplainPagerAdapter

    override fun onBinding() {
//        supportActionBar?.title = getString(R.string.reviews_and_complaint)

        setUpToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.reviews_and_complaint), showUpButton = false, showShadow = false)


        setUpViewpager(viewPager = mBinding.viewpager)

        setUpTabLayoutMediator(tabLayout = mBinding.tabLayout, viewPager = mBinding.viewpager)
    }

    /**
     * This method is used to setup ViewPager with [ReviewAndComplainPagerAdapter]
     */
    private fun setUpViewpager(viewPager: ViewPager2){
        mAdapter = ReviewAndComplainPagerAdapter(this)
        viewPager.adapter = mAdapter
    }

    /**
     * This method sets/links up the Tab Layout with ViewPager using [TabLayoutMediator]
     */
    private fun setUpTabLayoutMediator(tabLayout: TabLayout, viewPager: ViewPager2){
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.reviews_label)
                1 -> tab.text = getString(R.string.complaint_label)
            }
        }.attach()
    }

}