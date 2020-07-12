package com.ey.hotspot.ui.speed_test.wifi_log

import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentWifiLogBinding
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.ui.speed_test.rate_wifi.RateWifiFragment
import com.ey.hotspot.utils.replaceFragment

class WifiLogFragment : BaseFragment<FragmentWifiLogBinding, WifiLogViewModel>() {

    companion object {
        fun newInstance(wifiSsid: String) = WifiLogFragment().apply {
            arguments = Bundle().apply {
                putString(WIFI_SSID, wifiSsid)
            }
        }

        private const val WIFI_SSID = "wifi_ssid"
    }

    override fun getLayoutId() = R.layout.fragment_wifi_log
    override fun getViewModel() = WifiLogViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpToolbar(mBinding.toolbarLayout, getString(R.string.wifi_log_label), true)

        setDataInView()

        setUpListeners()
    }

    private fun setDataInView() {
        mBinding.tvWifiSsid.text = arguments?.getString(WIFI_SSID)
    }

    private fun setUpListeners() {
        //Rate Now
        mBinding.btnRateNow.setOnClickListener {
            replaceFragment(
                RateWifiFragment.newInstance(
                    wifiSsid = arguments?.getString(WIFI_SSID)!!,
                    wifiProvider = "Bleh",
                    location = "bleh"
                ),
                true
            )
        }

        //Report
        mBinding.ivFlag.setOnClickListener {
            replaceFragment(
                RaiseComplaintFragment.newInstance(
                    wifiSsid = arguments?.getString(WIFI_SSID)!!,
                    wifiProvider = "Bleh",
                    location = "bleh"
                ),
                true
            )
        }
    }
}