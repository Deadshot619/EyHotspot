package com.ey.hotspot.ui.search.searchlist

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.CoreApp
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.SearchListFragmentBinding
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.login.LoginActivity
import com.ey.hotspot.ui.review_and_complaint.reviews.ReviewsFragment
import com.ey.hotspot.ui.search.searchlist.adapter.SearchListAdapter
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.utils.constants.setUpSearchBar
import com.ey.hotspot.utils.dialogs.YesNoDialog
import com.ey.hotspot.utils.extention_functions.openNavigateUrl
import com.ey.hotspot.utils.extention_functions.parseToDouble
import com.ey.hotspot.utils.extention_functions.replaceFragment
import com.ey.hotspot.utils.extention_functions.shareWifiHotspotData

class SearchListFragment : BaseFragment<SearchListFragmentBinding, SearchListViewModel>() {

    private val dialog by lazy {
        YesNoDialog(requireActivity()).apply {
            setViews(
                title = getString(R.string.login_required),
                description = getString(R.string.need_to_login),
                yes = {
                    goToLoginScreen()
                },
                no = {
                    this.dismiss()
                })
        }
    }

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

        activity?.setUpSearchBar(mBinding.toolbarLayout, true) {
//            if (HotSpotApp.prefs?.getAppLoggedInStatus()!!)
//                mViewModel.getUserHotSpotResponse(it)
//            else
                mViewModel.getHotSpotResponse(it)
        }

        setUpRecyclerView(mBinding.rvSearchList)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        //Setup Adapter
        mAdapter = SearchListAdapter(object : SearchListAdapter.OnClickListener {
            //Rate Now button
            override fun onClickRateNow(data: GetHotSpotResponse) {

                if (HotSpotApp.prefs?.getAppLoggedInStatus()!!)
                    replaceFragment(
                        fragment = ReviewsFragment.newInstance(
                            locationId = data.id,
                            wifiSsid = data.name,
                            wifiProvider = data.provider_name,
                            location = data.location
                        ),
                        addToBackStack = true
                    )
                else
                    dialog.show()

            }

            //Report
            override fun onClickReport(data: GetHotSpotResponse) {

                if (HotSpotApp.prefs?.getAppLoggedInStatus()!!)
                    replaceFragment(
                        RaiseComplaintFragment.newInstance(
                            locationId = data.id,
                            wifiSsid = data.name,
                            wifiProvider = data.provider_name,
                            location = data.location
                        ), true
                    )
                else
                    dialog.show()
            }

            //Favourite
            override fun onClickAddFavourite(data: GetHotSpotResponse) {
                if (HotSpotApp.prefs?.getAppLoggedInStatus()!!)
                    mViewModel.markFavouriteItem(data.id)
                else
                    dialog.show()
            }

            //Navigate Now
            override fun onClickNavigate(data: GetHotSpotResponse) {
                activity?.openNavigateUrl(data.navigate_url)
            }

            override fun onClickShare(data: GetHotSpotResponse) {
                activity?.shareWifiHotspotData(id = data.id, lat = data.lat.parseToDouble(), lon = data.lng.parseToDouble())
            }
        })

        recyclerView.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this.adapter = mAdapter
        }
    }

    private fun goToLoginScreen() {
        //Clear Data
        HotSpotApp.prefs?.clearSharedPrefData()

        //Redirect user to Login Activity
        CoreApp.instance.startActivity(Intent(CoreApp.instance, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

}