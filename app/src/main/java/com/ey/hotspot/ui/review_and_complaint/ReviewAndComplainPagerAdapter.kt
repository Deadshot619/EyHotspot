package com.ey.hotspot.ui.review_and_complaint

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ey.hotspot.ui.review_and_complaint.complaint_list.ComplaintListFragment
import com.ey.hotspot.ui.review_and_complaint.review_list.ReviewListFragment

class ReviewAndComplainPagerAdapter(
    fragment: ReviewAndComplainFragment
) : FragmentStateAdapter(fragment) {

    private val listFragments =
        listOf(
            ReviewListFragment.newInstance(),
            ComplaintListFragment.newInstance()
        )

    override fun getItemCount(): Int = listFragments.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> listFragments[position].apply {
                arguments = Bundle().apply {
//                    putBoolean(Constants.ADDED_BY_ME, true)
                }
            }
            1 -> listFragments[position].apply {
                arguments = Bundle().apply {
//                    putBoolean(Constants.ADDED_BY_ME, false)
                }
            }
            else -> listFragments[0].apply {
                arguments = Bundle().apply {
//                    putBoolean(Constants.ADDED_BY_ME, false)
                }
            }
        }
    }
}