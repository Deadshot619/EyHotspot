package com.ey.hotspot.ui.review_and_complaint.review_list

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentReviewListBinding

class ReviewListFragment : BaseFragment<FragmentReviewListBinding, ReviewListViewModel>() {

    companion object {
        fun newInstance() = ReviewListFragment()
    }

    override fun getLayoutId() = R.layout.fragment_review_list
    override fun getViewModel() = ReviewListViewModel::class.java
    override fun onBinding() {

        setUpRecyclerView()
    }

    private fun setUpRecyclerView(){

    }
}