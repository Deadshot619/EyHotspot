package com.ey.hotspot.ui.search.searchlist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.SearchFragmentBinding
import com.ey.hotspot.databinding.SearchListFragmentBinding
import com.ey.hotspot.ui.registration.email_verification.EmailVerificationFragment
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