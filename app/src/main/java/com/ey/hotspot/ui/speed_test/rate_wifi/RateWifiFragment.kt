package com.ey.hotspot.ui.speed_test.rate_wifi

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRateWifiBinding
import com.ey.hotspot.network.response.LocationReviews
import com.ey.hotspot.utils.constants.ReviewTypeEnum
import com.ey.hotspot.utils.dialogs.YesNoDialog
import com.ey.hotspot.utils.extention_functions.removeFragment
import com.ey.hotspot.utils.extention_functions.showMessage

class RateWifiFragment : BaseFragment<FragmentRateWifiBinding, RateWifiViewModel>() {



    companion object {
        fun newInstance(locationId: Int, wifiSsid: String, wifiProvider: String, location: String, reviewType: ReviewTypeEnum, userReview: LocationReviews?) =
            RateWifiFragment().apply {
                arguments = Bundle().apply {
                    putInt(WIFI_ID, locationId)
                    putString(WIFI_SSID, wifiSsid)
                    putString(WIFI_PROVIDER, wifiProvider)
                    putString(LOCATION, location)
                    putString(REVIEW_TYPE, reviewType.value)
                    putParcelable(USER_REVIEW, userReview)
                }
            }

        private const val WIFI_ID = "wifi_id"
        private const val WIFI_SSID = "wifi_ssid"
        private const val WIFI_PROVIDER = "wifi_provider"
        private const val LOCATION = "location"
        private const val REVIEW_TYPE = "review_type"
        private const val USER_REVIEW = "user_review"
    }

    override fun getLayoutId() = R.layout.fragment_rate_wifi
    override fun getViewModel() = RateWifiViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.rate_wifi_label), showUpButton = true)


        setDataInView()

        setUpListeners()

        setUpObservers()
    }
    //Create 'Yes/No' Dialog
    val dialog by lazy {
        YesNoDialog(requireContext()).apply {
            setViews(
                title = getString(R.string.confirm_review),
                description = "",
                yes = { mViewModel.addReview()
                    this.dismiss() },
                no = { this.dismiss() }
            )
        }
    }

    //Method to set data in view
    private fun setDataInView(){
        mViewModel.rateWifiData.value?.run {
            id = arguments?.getInt(WIFI_ID) ?: -1
            wifiSsid = arguments?.getString(WIFI_SSID) ?: ""
            wifiProvider = arguments?.getString(WIFI_PROVIDER) ?: ""
            wifiLocation = arguments?.getString(LOCATION) ?: ""

            rating = arguments?.getParcelable<LocationReviews>(USER_REVIEW)?.rating ?: 0.0f
            feedback = arguments?.getParcelable<LocationReviews>(USER_REVIEW)?.description ?: ""
        }
    }


    private fun setUpListeners(){
        mBinding.run {
            //Cancel Button
            btnCancelButton.setOnClickListener {
                edtRemarks.setText("")
                activity?.onBackPressed()
            }

//            Submit Feedback
            btnSubmitFeedback.setOnClickListener {
                if (validate())
               dialog.show()
            }
        }
    }



    private fun setUpObservers() {
        //Simon, Go Back
        mViewModel.goBack.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {status ->
                if (status)

                    //activity?.onBackPressed()
                    removeFragment(this)

            }
        })
    }

    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        mViewModel.rateWifiData.value?.run {
            mBinding.run {
                return if (rating <= 0f) {
                    showMessage(resources.getString(R.string.enter_remark_error_label))
                    false
                } else if (feedback.trim().isEmpty()) {
//                    edtRemarks.error = resources.getString(R.string.enter_remark_error_label)
//                    false
                    true
                } else true
            }
        } ?: return false
    }
}