package com.ey.hotspot.ui.favourite

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FavouriteFragmentBinding
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.review_and_complaint.reviews.ReviewsFragment
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.utils.constants.setUpSearchBar
import com.ey.hotspot.utils.extention_functions.openNavigateUrl
import com.ey.hotspot.utils.extention_functions.replaceFragment
import com.ey.hotspot.utils.extention_functions.shareWifiHotspotData

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

        //Normal Toolbar
        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.favoutite_wifi_label),
            showUpButton = false,
            showShadow = false
        )

        //SearchBar
        activity?.setUpSearchBar(
            toolbarBinding = mBinding.toolbarLayout2,
            showUpButton = false,
            showShadow = false
        ) {
            mViewModel.getFavouriteList(value = it)
        }

        setUpRecyclerView(mBinding.rvFavouriteWifiList)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        //Setup Adapter
        mAdapter = FavouriteListAdapter(object : FavouriteListAdapter.OnClickListener {
            //Rate Now button
            override fun onClickRateNow(data: GetFavouriteItem) {
                replaceFragment(
                    fragment = ReviewsFragment.newInstance(
                        locationId = data.id,
                        wifiSsid = data.name,
                        wifiProvider = data.provider_name,
                        location = data.location
                    ),
                    addToBackStack = true
                )
            }

            //Report
            override fun onClickReport(data: GetFavouriteItem) {
                replaceFragment(
                    RaiseComplaintFragment.newInstance(
                        locationId = data.id,
                        wifiSsid = data.name,
                        wifiProvider = data.provider_name,
                        location = data.location
                    ), true
                )
            }

            //Favourite
            override fun onClickAddFavourite(data: GetFavouriteItem) {
                mViewModel.markFavouriteItem(data.id)
            }

            //Navigate Now
            override fun onClickNavigate(data: GetFavouriteItem) {
                activity?.openNavigateUrl(data.navigate_url)
            }

            //Share
            override fun onClickShare(data: GetFavouriteItem) {
                activity?.shareWifiHotspotData(id = data.id, lat = data.lat, lon = data.lng)
            }
        })

        recyclerView.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this.adapter = mAdapter
        }
    }
}