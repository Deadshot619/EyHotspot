package com.ey.hotspot.ui.review_and_complaint.review_list

import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentReviewListBinding
import com.ey.hotspot.utils.constants.ReviewSortType

class ReviewListFragment : BaseFragment<FragmentReviewListBinding, ReviewListViewModel>() {

    companion object {
        fun newInstance() = ReviewListFragment()
    }

    private lateinit var mAdapter: ReviewListAdapter
    var check = 0

    override fun getLayoutId() = R.layout.fragment_review_list
    override fun getViewModel() = ReviewListViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpRecyclerView(mBinding.rvReviewList)

        setUpListeners()
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        //Setup Adapter
        mAdapter = ReviewListAdapter()

        recyclerView.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this.adapter = mAdapter
        }
    }

    private fun setUpListeners() {
        mBinding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (++check > 1)
                    if (mBinding.spinnerSort.selectedItemPosition == 0)
                        mViewModel.getReviewsList(ReviewSortType.LATEST)
                    else
                        mViewModel.getReviewsList(ReviewSortType.OLDEST)
            }

        }
    }

}