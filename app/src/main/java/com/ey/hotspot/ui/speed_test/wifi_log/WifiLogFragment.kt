package com.ey.hotspot.ui.speed_test.wifi_log

import android.graphics.drawable.Drawable
import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.FragmentWifiLogBinding
import com.ey.hotspot.network.response.ValidateWifiResponse
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.review_and_complaint.reviews.ReviewsFragment
import com.ey.hotspot.ui.speed_test.raise_complaint.RaiseComplaintFragment
import com.ey.hotspot.ui.speed_test.test_result.TestResultsFragment
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListResponse
import com.ey.hotspot.utils.extention_functions.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WifiLogFragment : BaseFragment<FragmentWifiLogBinding, WifiLogViewModel>() {
    private var favouriteType: Boolean = false

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

   lateinit var wifiloglist:WifiLogListResponse
    override fun getLayoutId() = R.layout.fragment_wifi_log
    override fun getViewModel() = WifiLogViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpToolbar(mBinding.toolbarLayout, getString(R.string.wifi_log_label), true)

        setDataInView()


        setUpListeners()

        mBinding.ivFavourites.setOnClickListener{
            if (HotSpotApp.prefs?.getAppLoggedInStatus()!!) {
                if (wifiloglist.location!=null) {
                val imgID1: Drawable.ConstantState? =
                    requireContext().getDrawable(R.drawable.ic_favorite_filled_gray)
                        ?.getConstantState()
                val imgID2: Drawable.ConstantState? =
                    mBinding.ivFavourites.getDrawable().getConstantState()
                if (imgID1 == imgID2) {
                    mBinding.ivFavourites.setImageResource(R.drawable.ic_favorite_filled_red)
                    favouriteType = true
                } else {
                    mBinding.ivFavourites.setImageResource(R.drawable.ic_favorite_filled_gray)
                    favouriteType = false
                }

                    mViewModel.markFavouriteItem(wifiloglist.location?.id!!)
                }
                else
                {
                    showMessage("No data Available")
                }

            }
        }
    }

    private fun setDataInView() {
        wifiloglist=arguments?.getParcelable<WifiLogListResponse>(WIFI_LOG)!!
        if (!wifiloglist?.location?.name.isNullOrEmpty()) {
            mBinding.tvWifiSsid.text =wifiloglist?.location?.name
        }
        mBinding.tvDate.text="Date:${getDate(wifiloglist?.login_at!!.extractDateFromDateTime())}"
        mBinding.tvStartTimeValue.text=getTime(wifiloglist?.login_at?.extractTimeFromDateTime())

        mBinding.tvEndTimeValue.text =
                getTime(wifiloglist?.logout_at?.extractTimeFromDateTime().toString())
        mBinding.tvStartSpeedValue.text=getTime(wifiloglist?.login_at?.extractTimeFromDateTime())
        mBinding.tvEndSpeedValue.text="${wifiloglist?.location?.average_rating.toString().extractspeed()} mbps"
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
           if (wifiloglist.location!=null) {
               replaceFragment(
                   ReviewsFragment.newInstance(
                       locationId = (wifiloglist.location?.id!!.toString()).toInt(),
                       wifiSsid =wifiloglist.location?.name!!.toString(),
                       wifiProvider =wifiloglist.location?.provider_name!!.toString(),
                       location = wifiloglist.location?.name!!.toString()
                   ), true
               )
           }
            else
           {
               showMessage("No data Available")
           }
        }

        //Report
        mBinding.ivFlag.setOnClickListener {
            if (wifiloglist.location!=null) {
                replaceFragment(
                    RaiseComplaintFragment.newInstance(
                        locationId = (wifiloglist.location?.id!!.toString()).toInt(),
                        wifiSsid =wifiloglist.location?.name!!.toString(),
                        wifiProvider =wifiloglist.location?.provider_name!!.toString(),
                        location = wifiloglist.location?.name!!.toString()
                    ), true
                )
            }
            else
            {
                showMessage("No data Available")
            }
        }
    }
}

