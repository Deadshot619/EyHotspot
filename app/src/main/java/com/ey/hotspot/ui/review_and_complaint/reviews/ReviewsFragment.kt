package com.ey.hotspot.ui.review_and_complaint.reviews

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentReviewsBinding
import com.ey.hotspot.ui.speed_test.rate_wifi.RateWifiFragment
import com.ey.hotspot.utils.replaceFragment

class ReviewsFragment : BaseFragment<FragmentReviewsBinding, ReviewsViewModel>() {

    companion object {
        fun newInstance(locationId: Int, wifiSsid: String, wifiProvider: String, location: String) =
            ReviewsFragment().apply {
                arguments = Bundle().apply {
                    putInt(WIFI_ID, locationId)
                    putString(WIFI_SSID, wifiSsid)
                    putString(WIFI_PROVIDER, wifiProvider)
                    putString(LOCATION, location)
                }
            }

        private const val WIFI_ID = "wifi_id"
        private const val WIFI_SSID = "wifi_ssid"
        private const val WIFI_PROVIDER = "wifi_provider"
        private const val LOCATION = "location"
    }

    private lateinit var mAdapter: ReviewsAdapter

    override fun getLayoutId() = R.layout.fragment_reviews
    override fun getViewModel() = ReviewsViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.reviews_label), showUpButton = true)

        setDataInView()

        setUpRecyclerView(mBinding.rvReviewList)

        setUpClickListeners()

        mViewModel.getReviewsList()
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        //Setup Adapter
        mAdapter = ReviewsAdapter()

        recyclerView.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this.adapter = mAdapter
        }
    }

    //Method to set data in view
    private fun setDataInView(){
        mViewModel.rateWifiData.value?.run {
            id = arguments?.getInt(WIFI_ID) ?: -1
            wifiSsid = arguments?.getString(WIFI_SSID) ?: ""
            wifiProvider = arguments?.getString(WIFI_PROVIDER) ?: ""
            wifiLocation = arguments?.getString(LOCATION) ?: ""
        }
    }

    private fun setUpClickListeners(){
        mBinding.ivAdd.setOnClickListener {
            mViewModel.rateWifiData.value?.let {
                replaceFragment(
                    fragment = RateWifiFragment.newInstance(
                        locationId = it.id,
                        wifiSsid = it.wifiSsid,
                        wifiProvider = it.wifiProvider,
                        location = it.wifiLocation
                    ),
                    addToBackStack = true
                )
            }
        }
    }

}