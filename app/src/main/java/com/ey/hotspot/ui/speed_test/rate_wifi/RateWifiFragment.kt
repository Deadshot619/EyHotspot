package com.ey.hotspot.ui.speed_test.rate_wifi

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRateWifiBinding

class RateWifiFragment : BaseFragment<FragmentRateWifiBinding, RateWifiViewModel>() {

    companion object {
        fun newInstance() =
            RateWifiFragment()
    }

    override fun getLayoutId() = R.layout.fragment_rate_wifi
    override fun getViewModel() = RateWifiViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.rate_wifi_label), showUpButton = true)

    }
}