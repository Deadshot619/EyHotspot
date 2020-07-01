package com.ey.hotspot.ui.favourite.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FavouriteFragmentBinding
import com.ey.hotspot.ui.favourite.FavouriteViewModel
import com.ey.hotspot.ui.favourite.adapter.FavouriteAdapter
import com.ey.hotspot.ui.search.searchlist.SearchListFragment
import com.ey.hotspot.ui.search.searchlist.adapter.SearchListAdapter
import com.ey.hotspot.ui.search.searchlist.model.SearchList
import kotlinx.android.synthetic.main.search_list_fragment.*

class FavouriteFragment : BaseFragment<FavouriteFragmentBinding, FavouriteViewModel>() {


    private val usersAdapter = FavouriteAdapter(arrayListOf())

    companion object {
        fun newInstance() = FavouriteFragment()
    }


    override fun getLayoutId(): Int {
        return R.layout.favourite_fragment
    }


    override fun getViewModel(): Class<FavouriteViewModel> {

        return FavouriteViewModel::class.java
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

        for (i in 0..9) {
            list.add(SearchList("Avator", "prashantj@gmail.com", "prashant", 1, "KK"))
        }

        return list;
    }

}