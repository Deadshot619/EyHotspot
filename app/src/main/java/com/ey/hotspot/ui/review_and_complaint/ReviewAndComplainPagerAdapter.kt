package com.ey.hotspot.ui.review_and_complaint

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ey.hotspot.ui.review_and_complaint.review_list.ReviewListFragment

class ReviewAndComplainPagerAdapter(
    fragment: ReviewAndComplainFragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ReviewListFragment().apply {
                arguments = Bundle().apply {
//                    putBoolean(Constants.ADDED_BY_ME, true)
                }
            }
            else -> ReviewListFragment().apply {
                arguments = Bundle().apply {
//                    putBoolean(Constants.ADDED_BY_ME, false)
                }
            }
        }
    }
}