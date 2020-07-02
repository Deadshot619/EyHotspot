package com.ey.hotspot.ui.speed_test.wifi_log

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentWifiLogBinding

class WifiLogFragment : BaseFragment<FragmentWifiLogBinding, WifiLogViewModel>() {

    companion object {
        fun newInstance() = WifiLogFragment()
    }

    override fun getLayoutId() = R.layout.fragment_wifi_log
    override fun getViewModel() = WifiLogViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel


    }
}