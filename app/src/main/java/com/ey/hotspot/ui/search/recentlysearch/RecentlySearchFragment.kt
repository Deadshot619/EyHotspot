package com.ey.hotspot.ui.search.recentlysearch

import androidx.recyclerview.widget.LinearLayoutManager
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.RecentlySearchFragmentBinding
import com.ey.hotspot.ui.search.recentlysearch.adapter.RecentlySearchListAdapter
import com.ey.hotspot.ui.search.recentlysearch.model.RecentlySearch
import com.ey.hotspot.ui.search.searchlist.adapter.SearchListAdapter
import com.ey.hotspot.ui.search.searchlist.model.SearchList
import kotlinx.android.synthetic.main.recently_search_fragment.*
import kotlinx.android.synthetic.main.search_list_fragment.*

class RecentlySearchFragment :
    BaseFragment<RecentlySearchFragmentBinding, RecentlySearchViewModel>() {

    private val usersAdapter = RecentlySearchListAdapter(arrayListOf())


    override fun getLayoutId(): Int {
        return R.layout.recently_search_fragment
    }

    override fun getViewModel(): Class<RecentlySearchViewModel> {
        return RecentlySearchViewModel::class.java
    }

    override fun onBinding() {
        setUpSearchBar(mBinding.toolbarLayout, true, {})


        rvRecentlySearchView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
            usersAdapter.updateUsers(sendData())
        }
    }

    private fun sendData(): ArrayList<RecentlySearch> {
        val list = arrayListOf<RecentlySearch>()
        for (i in 0..9) {
            list.add(RecentlySearch("Star Buck Wifi"))
        }
        return list;
    }
}