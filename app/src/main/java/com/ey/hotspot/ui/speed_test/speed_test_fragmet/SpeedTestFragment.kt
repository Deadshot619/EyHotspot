package com.ey.hotspot.ui.speed_test.speed_test_fragmet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentSpeedTestBinding
import com.ey.hotspot.ui.speed_test.test_result.TestResultsFragment
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.setUpToolbar
import com.ey.hotspot.utils.extention_functions.extractWifiName
import com.ey.hotspot.utils.extention_functions.getUserLocation
import com.ey.hotspot.utils.extention_functions.replaceFragment

class SpeedTestFragment : BaseFragment<FragmentSpeedTestBinding, SpeedTestFragmentViewModel>() {

    private lateinit var connec: ConnectivityManager
    private lateinit var wifiManager: WifiManager
    private lateinit var networkRequestWiFi: NetworkRequest


    companion object {
        fun newInstance() = SpeedTestFragment()
    }

    override fun getLayoutId() = R.layout.fragment_speed_test
    override fun getViewModel() = SpeedTestFragmentViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        activity?.setUpToolbar(
            mBinding.toolbarLayout,
            resources.getString(R.string.speed_test_label),
            false,
            showTextButton = false
        )

        setUpListeners()

        //Get connectivity Manager
        connec = requireActivity().applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //Get Wifi Manager
        wifiManager =
            requireActivity().applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        networkRequestWiFi = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }

    override fun onStart() {
        super.onStart()
        connec.registerNetworkCallback(networkRequestWiFi, networkCallbackWiFi)
    }

    fun setUpListeners() {
        //Go
        mBinding.tvGo.setOnClickListener {
            replaceFragment(TestResultsFragment(), true)
        }
    }

    override fun onStop() {
        super.onStop()
        connec.unregisterNetworkCallback(networkCallbackWiFi)
    }

    //Network callback for WIFI
    private var networkCallbackWiFi = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            //get wifi ssid
            val wifiSSid = wifiManager.connectionInfo.ssid.extractWifiName()
            if (wifiSSid.contains(Constants.UNKNOWN_SSID)) {

            } else {
                requireActivity().applicationContext.getUserLocation { lat, lng ->
                    if (lat != null && lng != null)
                        mViewModel.verifyHotspot(wifiSSid, lat, lng)
                    else
                        mViewModel.verifyHotspot(wifiSSid, Constants.LATITUDE, Constants.LONGITUDE)
                }
            }

        }
    }
}