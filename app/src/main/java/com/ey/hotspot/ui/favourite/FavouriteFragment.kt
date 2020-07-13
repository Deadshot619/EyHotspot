package com.ey.hotspot.ui.favourite

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FavouriteFragmentBinding
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.ui.speed_test.rate_wifi.RateWifiFragment
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

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.favoutite_wifi_label),
            showUpButton = false
        )

        setUpRecyclerView(mBinding.rvFavouriteWifiList)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        //Setup Adapter
        mAdapter = FavouriteListAdapter(object : FavouriteListAdapter.OnClickListener {
            //Rate Now button
            override fun onClickRateNow(data: GetFavouriteItem) {
                replaceFragment(
                    fragment = RateWifiFragment.newInstance(
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
                val url = data.navigate_url

                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        })

        recyclerView.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this.adapter = mAdapter
        }
    }
}