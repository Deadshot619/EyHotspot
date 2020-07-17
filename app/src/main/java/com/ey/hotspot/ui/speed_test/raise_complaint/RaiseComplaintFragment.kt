package com.ey.hotspot.ui.speed_test.raise_complaint

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRaiseComplaintBinding
import com.ey.hotspot.utils.showMessage

class RaiseComplaintFragment :
    BaseFragment<FragmentRaiseComplaintBinding, RaiseComplaintViewModel>() {

    companion object {
        fun newInstance(locationId: Int, wifiSsid: String, wifiProvider: String, location: String) =
            RaiseComplaintFragment().apply {
                arguments = Bundle().apply {
                    putInt(WIFI_ID, locationId)
                    putString(WIFI_SSID, wifiSsid)
                    putString(WIFI_PROVIDER, wifiProvider)
                    putString(LOCATION, location)
                }
            }

        private const val WIFI_ID = "wifi_id"
        private const val WIFI_SSID = "wifi_ssid"
        private const val WIFI_PROVIDER = "wifi_provider"
        private const val LOCATION = "location"
    }

    override fun getLayoutId() = R.layout.fragment_raise_complaint
    override fun getViewModel() = RaiseComplaintViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.comment_and_evaluate_label),
            showUpButton = true
        )

        setDataInView()

        setUpListeners()

        setUpObservers()
    }


    //Method to set data in view
    private fun setDataInView() {
        mViewModel.raiseComplaintData.value?.run {
            id = arguments?.getInt(RaiseComplaintFragment.WIFI_ID) ?: -1
            wifiSsid = arguments?.getString(RaiseComplaintFragment.WIFI_SSID) ?: ""
            wifiProvider = arguments?.getString(RaiseComplaintFragment.WIFI_PROVIDER) ?: ""
            wifiLocation = arguments?.getString(RaiseComplaintFragment.LOCATION) ?: ""
        }
    }

    private fun setUpListeners() {
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

            //Issues Spinner
            spinnerIssue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    mViewModel.raiseComplaintData.value?.issueType =
                        mViewModel.issuesTypes.value?.types?.find { it.value == spinnerIssue.selectedItem.toString() }?.key
                            ?: ""
                }

            }
        }
    }

    private fun setUpObservers() {
        mViewModel.issuesTypes.observe(viewLifecycleOwner, Observer {
            it?.let {
                val adapter = ArrayAdapter<String>(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item,
                    it.types.map { it.value }.toList()
                )

                mBinding.spinnerIssue.adapter = adapter
            }
        })

        //Simon, Go Back
        mViewModel.goBack.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {status ->
                if (status)
                    activity?.onBackPressed()
            }
        })
    }


    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        mViewModel.raiseComplaintData.value?.run {
            mBinding.run {
                return when {
                    issueType.trim().isEmpty() -> {
                        showMessage("Please select a issue")
                        false
                    }
                    feedback.trim().isEmpty() -> {
                        edtRemarks.error = resources.getString(R.string.enter_remark_error_label)
                        false
                    }
                    else -> true
                }
            }
        } ?: return false
    }

}