package com.ey.hotspot.ui.speed_test.speed_test_fragmet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.view.View
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentSpeedTestBinding
import com.ey.hotspot.ui.speed_test.test_result.TestResultsFragment
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.checkWifiContainsKeywords
import com.ey.hotspot.utils.constants.setUpToolbar
import com.ey.hotspot.utils.dialogs.YesNoDialog
import com.ey.hotspot.utils.extention_functions.*

class SpeedTestFragment : BaseFragment<FragmentSpeedTestBinding, SpeedTestFragmentViewModel>() {

    private lateinit var connec: ConnectivityManager
    private lateinit var wifiManager: WifiManager
    private lateinit var networkRequestWiFi: NetworkRequest


    val dialog by lazy {
        YesNoDialog(this.requireActivity()).apply {
            setViews(
                title = getString(R.string.wifi_disabled_label),
                description = getString(R.string.wifi_enable_conformation),
                yes = {
                    wifiManager.isWifiEnabled = true
                    this.dismiss()
                },
                no = {
                    this.dismiss()
                }
            )
        }
    }


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
        setUpObservers()

        //Get connectivity Manager
        connec =
            requireActivity().applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        //Get Wifi Manager
        wifiManager =
            requireActivity().applicationContext?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        networkRequestWiFi = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()


        // Prompt the user for permission.

        activity?.checkLocationPermission(view = mBinding.root, func = {
                checkWifiEnabled()
                if (!requireActivity().isLocationEnabled()) {
                    activity?.turnOnGpsDialog()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        //Receiver for wifi
        connec.registerNetworkCallback(networkRequestWiFi, networkCallbackWiFi)

        //Receiver for GPS
//        activity?.registerReceiver(gpsReceiver, IntentFilter(LocationManager.MODE_CHANGED_ACTION))
    }

    fun setUpListeners() {
        //Go
        mBinding.tvGo.setOnClickListener {
            replaceFragment(
                TestResultsFragment.newInstance(
                    mViewModel.wifiData.value,
                    mViewModel.hideDataView.value!!
                ), true
            )
        }
    }

    fun setUpObservers() {
        mViewModel.hideDataView.observe(viewLifecycleOwner, Observer {
            if (it){
                mBinding.clDataLayout.visibility = View.INVISIBLE
                mBinding.tvWifiNotValidated.visibility = View.VISIBLE
            }else{
                mBinding.clDataLayout.visibility = View.VISIBLE
                mBinding.tvWifiNotValidated.visibility = View.GONE
            }
        })
    }

    override fun onStop() {
        super.onStop()

        connec.unregisterNetworkCallback(networkCallbackWiFi)
//        activity?.unregisterReceiver(gpsReceiver)
    }

    //Method to check if wifi is enabled
    private fun checkWifiEnabled() {
        if (!wifiManager.isWifiEnabled)
            dialog.show()
    }

    //Network callback for WIFI
    private var networkCallbackWiFi = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            //get wifi ssid
            val wifiSSid = wifiManager.connectionInfo.ssid.extractWifiName()
            if (wifiSSid.contains(Constants.UNKNOWN_SSID)) {
                if (!requireActivity().isLocationEnabled()) {
                    activity?.turnOnGpsDialog()
                    if (checkWifiContainsKeywords(wifiSSid))
                        getUserLocationAndValidateWifi(wifiSSid)
                }
            } else {//TODO 12/08/20 : Uncomment this & remove turtlemint
                if (checkWifiContainsKeywords(wifiSSid))
                    getUserLocationAndValidateWifi("$wifiSSid-Turtlemint")
            }
        }
    }

    //Method to get User Location & validate Wifi
    private fun getUserLocationAndValidateWifi(wifiSSid: String) {
        requireActivity().applicationContext.getUserLocation { lat, lng ->
            if (lat != null && lng != null)
                mViewModel.verifyHotspot(wifiSSid, lat, lng)
            /*else
                mViewModel.verifyHotspot(wifiSSid, Constants.LATITUDE, Constants.LONGITUDE)*/
        }
    }
}