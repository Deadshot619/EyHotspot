package com.ey.hotspot.ui.speed_test.test_result

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentTestResultsBinding
import com.ey.hotspot.network.response.ValidateWifiResponse
import com.ey.hotspot.utils.constants.setUpToolbar
import java.math.BigDecimal

class TestResultsFragment : BaseFragment<FragmentTestResultsBinding, TestResultsViewModel>() {

    companion object {
        fun newInstance(wifiData: ValidateWifiResponse?, hideDataView: Boolean = false) =
            TestResultsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(WIFI_DATA, wifiData)
                    putBoolean(HIDE_DATA_VIEW, hideDataView)
                }
            }

        private const val WIFI_DATA = "wifi_data"
        private const val HIDE_DATA_VIEW = "hide_data_view"
    }

    override fun getLayoutId() = R.layout.fragment_test_results
    override fun getViewModel() = TestResultsViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        activity?.setUpToolbar(
            mBinding.toolbarLayout,
            resources.getString(R.string.speed_test_label),
            true,
            showTextButton = true
        )

        mViewModel.setWifiData(
            arguments?.getParcelable<ValidateWifiResponse>(WIFI_DATA), arguments?.getBoolean(
                HIDE_DATA_VIEW
            )
        )

        setUpListeners()
        setUpObservers()
    }

    private fun setUpListeners() {
//        Toolbar retest button
        mBinding.tvTextButton.setOnClickListener {
            mViewModel.setSpeedValue(BigDecimal.valueOf(0))
            mViewModel.onCheckSpeedClick()
        }
    }


    private fun setUpObservers() {
        //Download Speed
        mViewModel.downloadSpeed.observe(viewLifecycleOwner, Observer {
            mBinding.imageSpeedometer.speedTo(it.toFloat(), 500)
        })

        //Download Completed
        mViewModel.downloadCompleted.observe(viewLifecycleOwner, Observer {
            //When true, show DownloadComplete Layout & hide Speedometer Layout else vice versa
            if (it) {
                mBinding.clSpeedometerLayout.visibility = View.INVISIBLE
                mBinding.clDownloadCompletedLayout.visibility = View.VISIBLE
                mBinding.tvTextButton.isEnabled = true
            } else {
                mBinding.clSpeedometerLayout.visibility = View.VISIBLE
                mBinding.clDownloadCompletedLayout.visibility = View.INVISIBLE
                mBinding.tvTextButton.isEnabled = false
            }
        })

        //Hide View
        mViewModel.hideDataView.observe(viewLifecycleOwner, Observer {
            if (it) {
                mBinding.clDataLayout.visibility = View.INVISIBLE
                mBinding.tvWifiNotValidated.visibility = View.VISIBLE
            } else {
                mBinding.clDataLayout.visibility = View.VISIBLE
                mBinding.tvWifiNotValidated.visibility = View.INVISIBLE
            }
        })
    }
}