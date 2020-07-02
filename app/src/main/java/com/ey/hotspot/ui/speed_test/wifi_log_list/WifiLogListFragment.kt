package com.ey.hotspot.ui.speed_test.wifi_log_list

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentWifiLogListBinding

class WifiLogListFragment : BaseFragment<FragmentWifiLogListBinding, WifiLogListViewModel>() {

    companion object {
        fun newInstance() = WifiLogListFragment()
    }

    override fun getLayoutId() = R.layout.fragment_wifi_log_list
    override fun getViewModel() = WifiLogListViewModel::class.java
    override fun onBinding() {

    }

}