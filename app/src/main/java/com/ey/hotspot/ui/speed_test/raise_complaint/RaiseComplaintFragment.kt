package com.ey.hotspot.ui.speed_test.raise_complaint

import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRaiseComplaintBinding
import com.ey.hotspot.utils.showMessage

class RaiseComplaintFragment : BaseFragment<FragmentRaiseComplaintBinding, RaiseComplaintViewModel>() {

    companion object {
        fun newInstance(wifiSsid: String, wifiProvider: String, location: String) =
            RaiseComplaintFragment().apply {
                arguments = Bundle().apply {
                    putString(WIFI_SSID, wifiSsid)
                    putString(WIFI_PROVIDER, wifiProvider)
                    putString(LOCATION, location)
                }
            }

        private const val WIFI_SSID = "wifi_ssid"
        private const val WIFI_PROVIDER = "wifi_provider"
        private const val LOCATION = "location"
    }

    override fun getLayoutId() = R.layout.fragment_raise_complaint
    override fun getViewModel() = RaiseComplaintViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.comment_and_evaluate_label), showUpButton = true)

        setDataInView()

        setUpListeners()
    }


    //Method to set data in view
    private fun setDataInView(){
        mViewModel.raiseComplaintData.value?.run {
            wifiSsid = arguments?.getString(RaiseComplaintFragment.WIFI_SSID) ?: ""
            wifiProvider = arguments?.getString(RaiseComplaintFragment.WIFI_PROVIDER) ?: ""
            wifiLocation = arguments?.getString(RaiseComplaintFragment.LOCATION) ?: ""
        }
    }

    private fun setUpListeners(){
        mBinding.run {
            //Cancel Button
            btnCancelButton.setOnClickListener {
                edtRemarks.setText("")
            }

//            Submit Feedback
            btnSubmitComplaint.setOnClickListener {
                if (validate())
                    mViewModel.addComplaint()
            }
        }
    }

    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        mViewModel.raiseComplaintData.value?.run {
            mBinding.run {
                return if (issueType.trim().isEmpty()) {
                    showMessage("Please select a issue")
                    false
                } else if (feedback.trim().isEmpty()) {
                    edtRemarks.error = resources.getString(R.string.enter_remark_error_label)
                    false
                } else true
            }
        } ?: return false
    }

}