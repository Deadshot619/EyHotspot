package com.ey.hotspot.ui.speed_test.wifi_log

import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentWifiLogBinding
import com.ey.hotspot.ui.review_and_complaint.reviews.ReviewsFragment
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.ui.speed_test.rate_wifi.RateWifiFragment
import com.ey.hotspot.utils.extention_functions.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WifiLogFragment : BaseFragment<FragmentWifiLogBinding, WifiLogViewModel>() {

    companion object {
        fun newInstance(
            wifiSsid: String,
            loginAt: String, logoutAt: String, createdAt: String, updateAt: String,
            averageSpeed: Double
        ) = WifiLogFragment().apply {
            arguments = Bundle().apply {
                putString(WIFI_SSID, wifiSsid)
                putString(LOGINAT,loginAt)
                putString(LOGOUTAT,logoutAt)
                putString(CREATEDAT,createdAt)
                putString(UPDATEAT,updateAt)
                putDouble(AVERAGE_SPPED,averageSpeed)
            }
        }
        private const val WIFI_SSID = "wifi_ssid"
        private const val LOGINAT="login_at"
        private const val LOGOUTAT="logout_at"
        private const val CREATEDAT="created_at"
        private const val UPDATEAT="update_at"
        private const val AVERAGE_SPPED="average_speed"
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
        mBinding.tvDate.text="Date:${getDate(arguments?.getString(LOGINAT)?.extractDateFromDateTime())}"
        mBinding.tvStartTimeValue.text=getTime(arguments?.getString(LOGINAT)?.extractTimeFromDateTime())
        mBinding.tvEndTimeValue.text=getTime(arguments?.getString(LOGOUTAT)?.extractTimeFromDateTime())
        mBinding.tvStartSpeedValue.text=getTime(arguments?.getString(LOGINAT)?.extractTimeFromDateTime())
        mBinding.tvEndSpeedValue.text="${arguments?.getDouble(AVERAGE_SPPED).toString().extractspeed()} mbps"
    }

    private fun getDate(datestring: String?):String
    {
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = inputFormat.parse(datestring)
        val outputDateStr: String = outputFormat.format(date)
        return outputDateStr;
    }
    private fun getTime(datestring: String?):String
    {
        val inputFormat: DateFormat = SimpleDateFormat("hh:mm:ss")
        val outputFormat: DateFormat = SimpleDateFormat("hh:mm a")
        val date: Date = inputFormat.parse(datestring)
        val outputDateStr: String = outputFormat.format(date)
        return outputDateStr;
    }

    private fun setUpListeners() {
        //Rate Now
        mBinding.btnRateNow.setOnClickListener {
            replaceFragment(
                ReviewsFragment.newInstance(
                    locationId = -1,
                    wifiSsid = arguments?.getString(WIFI_SSID)!!,
                    wifiProvider = "Bleh",
                    location = arguments?.getString(WIFI_SSID)!!
                ), true
            )
        }

        //Report
        mBinding.ivFlag.setOnClickListener {
            replaceFragment(
                RaiseComplaintFragment.newInstance(
                    locationId = -1,
                    wifiSsid = arguments?.getString(WIFI_SSID)!!,
                    wifiProvider = "Bleh",
                    location = "bleh"
                ),
                true
            )
        }
    }
}