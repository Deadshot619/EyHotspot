package com.ey.hotspot.ui.review_and_complaint.complaint_list

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentComplaintListBinding
import com.ey.hotspot.ui.review_and_complaint.review_list.ReviewListAdapter

class ComplaintListFragment : BaseFragment<FragmentComplaintListBinding, ComplaintListViewModel>() {

    companion object {
        fun newInstance() = ComplaintListFragment()
    }


    private lateinit var mAdapter: ReviewListAdapter

    override fun getLayoutId() = R.layout.fragment_complaint_list
    override fun getViewModel() = ComplaintListViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

//        setUpRecyclerView(mBinding.rvReviewList)
    }
}