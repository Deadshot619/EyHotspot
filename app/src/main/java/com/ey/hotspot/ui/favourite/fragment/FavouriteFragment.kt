package com.ey.hotspot.ui.favourite.fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FavouriteFragmentBinding
import com.ey.hotspot.ui.favourite.FavouriteActivityViewModel
import com.ey.hotspot.ui.favourite.adapter.FavouriteAdapter
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.search.searchlist.model.SearchList
import com.ey.hotspot.utils.showMessage
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

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }



        mViewModel.getFavouriteList()
        setUpObserver()


    }

    private fun setUpObserver() {


        mViewModel.getFavouriteResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {
                setUpRecyclearView(it.data)
            } else {
                showMessage(it.message, true)
            }
        })
    }

    private fun setUpRecyclearView(getFavouriteItem: List<GetFavouriteItem>) {

        rvSearchList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = usersAdapter
            usersAdapter.updateUsers(getFavouriteItem)
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