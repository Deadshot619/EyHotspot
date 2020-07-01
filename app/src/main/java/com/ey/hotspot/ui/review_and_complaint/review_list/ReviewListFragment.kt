package com.ey.hotspot.ui.review_and_complaint.review_list

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentReviewListBinding

class ReviewListFragment : BaseFragment<FragmentReviewListBinding, ReviewListViewModel>() {

    companion object {
        fun newInstance() = ReviewListFragment()
    }

    private lateinit var mAdapter: ReviewListAdapter

    override fun getLayoutId() = R.layout.fragment_review_list
    override fun getViewModel() = ReviewListViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpRecyclerView(mBinding.rvReviewList)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView){
        //Setup Adapter
        mAdapter = ReviewListAdapter()

        recyclerView.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this.adapter = mAdapter
        }
    }

}