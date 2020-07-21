package com.ey.hotspot.ui.review_and_complaint.complaint_list

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentComplaintListBinding

class ComplaintListFragment : BaseFragment<FragmentComplaintListBinding, ComplaintListViewModel>() {

    companion object {
        fun newInstance() = ComplaintListFragment()
    }


    private lateinit var mAdapter: ComplaintListAdapter

    override fun getLayoutId() = R.layout.fragment_complaint_list
    override fun getViewModel() = ComplaintListViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpRecyclerView(mBinding.rvComplaintList)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView){
        //Setup Adapter
        mAdapter = ComplaintListAdapter()

        recyclerView.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this.adapter = mAdapter
        }
    }
}