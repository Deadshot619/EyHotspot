package com.ey.hotspot.ui.speed_test.wifi_log

import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentWifiLogBinding
import com.ey.hotspot.network.response.ValidateWifiResponse
import com.ey.hotspot.ui.review_and_complaint.reviews.ReviewsFragment
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.ui.speed_test.test_result.TestResultsFragment
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListResponse
import com.ey.hotspot.utils.extention_functions.extractDateFromDateTime
import com.ey.hotspot.utils.extention_functions.extractTimeFromDateTime
import com.ey.hotspot.utils.extention_functions.extractspeed
import com.ey.hotspot.utils.extention_functions.replaceFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WifiLogFragment : BaseFragment<FragmentWifiLogBinding, WifiLogViewModel>() {

    companion object {
        fun newInstance(
           wifilog:WifiLogListResponse
        ) = WifiLogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(WIFI_LOG, wifilog)

            }
        }

        private const val WIFI_LOG = "wifi_LOG"

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

        if (!arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.name.isNullOrEmpty()) {
            mBinding.tvWifiSsid.text = arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.name
        }
        mBinding.tvDate.text="Date:${getDate(arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.login_at!!.extractDateFromDateTime())}"
        mBinding.tvStartTimeValue.text=getTime(arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.login_at?.extractTimeFromDateTime())

        mBinding.tvEndTimeValue.text =
                getTime(arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.logout_at?.extractTimeFromDateTime())
        mBinding.tvStartSpeedValue.text=getTime(arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.login_at?.extractTimeFromDateTime())
        mBinding.tvEndSpeedValue.text="${arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.average_rating.toString().extractspeed()} mbps"
    }

    private fun getDate(datestring: String?): String {
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = inputFormat.parse(datestring)
        val outputDateStr: String = outputFormat.format(date)
        return outputDateStr;
    }
    private fun getTime(datestring: String?):String
    {
        var outputDateStr: String=""
        if (!datestring.equals("null")) {
            val inputFormat: DateFormat = SimpleDateFormat("hh:mm:ss")
            val outputFormat: DateFormat = SimpleDateFormat("hh:mm a")
            val date: Date = inputFormat.parse(datestring)
             outputDateStr = outputFormat.format(date)
        }
        else
        {
            outputDateStr="-"
        }
        return outputDateStr;
    }

    private fun setUpListeners() {
        //Rate Now
        mBinding.btnRateNow.setOnClickListener {
            replaceFragment(
                ReviewsFragment.newInstance(
                    locationId = arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.id!!.toInt(),
                    wifiSsid = arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.name!!,
                    wifiProvider =  arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.provider_name!!,
                    location =  arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.name!!
                ), true
            )
        }

        //Report
        mBinding.ivFlag.setOnClickListener {
            replaceFragment(
                RaiseComplaintFragment.newInstance(
                    locationId = arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.id!!.toInt(),
                    wifiSsid = arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.name!!,
                    wifiProvider =  arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.provider_name!!,
                    location =  arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)?.location?.name!!
                ),
                true
            )
        }
    }
}