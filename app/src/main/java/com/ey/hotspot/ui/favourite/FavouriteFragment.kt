package com.ey.hotspot.ui.favourite

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FavouriteFragmentBinding
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.utils.replaceFragment

class FavouriteFragment : BaseFragment<FavouriteFragmentBinding, FavouriteViewModel>() {


    private lateinit var mAdapter: FavouriteListAdapter

    companion object {
        fun newInstance() = FavouriteFragment()
    }


    override fun getLayoutId() = R.layout.favourite_fragment
    override fun getViewModel() = FavouriteViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.favoutite_wifi_label), showUpButton = false)

        setUpRecyclerView(mBinding.rvFavouriteWifiList)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView){
        //Setup Adapter
        mAdapter = FavouriteListAdapter(object : FavouriteListAdapter.OnClickListener {
            //Rate Now button
            override fun onClickRateNow() {
                replaceFragment(RaiseComplaintFragment(), true)
            }
        })

        recyclerView.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this.adapter = mAdapter
        }
    }
}