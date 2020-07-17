package com.ey.hotspot.ui.search.searchlist

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.SearchListFragmentBinding
import com.ey.hotspot.ui.home.models.GetUserHotSpotResponse
import com.ey.hotspot.ui.search.searchlist.adapter.SearchListAdapter
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.ui.speed_test.rate_wifi.RateWifiFragment
import com.ey.hotspot.utils.replaceFragment

class SearchListFragment : BaseFragment<SearchListFragmentBinding, SearchListViewModel>() {

//    private val usersAdapter = SearchListAdapter(arrayListOf())
    private lateinit var mAdapter: SearchListAdapter

    companion object {
        fun newInstance() = SearchListFragment()
    }

    override fun getLayoutId() = R.layout.search_list_fragment
    override fun getViewModel() = SearchListViewModel::class.java

    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpSearchBar(mBinding.toolbarLayout,true){
            if(HotSpotApp.prefs?.getAppLoggedInStatus()!!)
                mViewModel.getUserHotSpotResponse(it)
            else
                mViewModel.getHotSpotResponse(it)
        }

        setUpRecyclerView(mBinding.rvSearchList)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView){
        //Setup Adapter
        mAdapter = SearchListAdapter(object : SearchListAdapter.OnClickListener {
            //Rate Now button
            override fun onClickRateNow(data: GetUserHotSpotResponse) {
                replaceFragment(
                    fragment = RateWifiFragment.newInstance(
                        locationId = data.id,
                        wifiSsid = data.name,
                        wifiProvider = data.provider_name,
                        location = data.location
                    ),
                    addToBackStack = true
                )
            }

            //Report
            override fun onClickReport(data: GetUserHotSpotResponse) {
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
            override fun onClickAddFavourite(data: GetUserHotSpotResponse) {
//                mViewModel.markFavouriteItem(data.id)
            }

            //Navigate Now
            override fun onClickNavigate(data: GetUserHotSpotResponse) {
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