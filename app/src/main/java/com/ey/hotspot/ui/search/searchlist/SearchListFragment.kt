package com.ey.hotspot.ui.search.searchlist

import androidx.recyclerview.widget.LinearLayoutManager
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.SearchListFragmentBinding
import com.ey.hotspot.ui.search.searchlist.adapter.SearchListAdapter
import com.ey.hotspot.ui.search.searchlist.model.SearchList
import kotlinx.android.synthetic.main.search_list_fragment.*

class SearchListFragment : BaseFragment<SearchListFragmentBinding, SearchListViewModel>() {

    private val usersAdapter = SearchListAdapter(arrayListOf())

    companion object {
        fun newInstance() = SearchListFragment()
    }

    override fun getLayoutId(): Int {

        return R.layout.search_list_fragment
    }

    override fun getViewModel(): Class<SearchListViewModel> {

        return SearchListViewModel::class.java
    }

    override fun onBinding() {

        setUpSearchBar(mBinding.toolbarLayout,true){}

        rvSearchList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
            usersAdapter.updateUsers(sendData())
        }

    }

    private fun sendData(): ArrayList<SearchList> {
        val list = arrayListOf<SearchList>()
        for(i in 0..9){
            list.add(SearchList("Avator","prashantj@gmail.com","prashant",1,"KK"))
        }
        return list;
    }



}