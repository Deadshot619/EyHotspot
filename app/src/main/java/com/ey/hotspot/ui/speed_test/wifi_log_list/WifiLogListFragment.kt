package com.ey.hotspot.ui.speed_test.wifi_log_list

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentWifiLogListBinding
import com.ey.hotspot.ui.speed_test.wifi_log.WifiLogFragment
import com.ey.hotspot.utils.replaceFragment

class WifiLogListFragment : BaseFragment<FragmentWifiLogListBinding, WifiLogListViewModel>() {

    companion object {
        fun newInstance() = WifiLogListFragment()
    }

    private lateinit var mAdapter: WifiLogListAdapter


    override fun getLayoutId() = R.layout.fragment_wifi_log_list
    override fun getViewModel() = WifiLogListViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.wifi_log_list_label), showUpButton = true)

        setUpRecyclerView(mBinding.rvWifiLogList)
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView){
        //Setup Adapter
        mAdapter = WifiLogListAdapter(WifiLogListAdapter.OnClickListener {
            replaceFragment(WifiLogFragment.newInstance(it.wifiSsid), true)
        })

        recyclerView.run {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            this.adapter = mAdapter
        }
    }

}