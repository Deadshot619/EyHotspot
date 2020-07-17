package com.ey.hotspot.ui.login.otpverification.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.OTPVerificationFragmentBinding
import com.ey.hotspot.ui.login.otpverification.fragment.model.SendOTPRequest
import com.ey.hotspot.ui.login.otpverification.fragment.model.VerifyOTPRequest
import com.ey.hotspot.ui.speed_test.rate_wifi.RateWifiFragment
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage
import com.facebook.login.LoginFragment

class OTPVerificationFragment :
    BaseFragment<OTPVerificationFragmentBinding, OTPVerificationViewModel>() {


    companion object {
        fun newInstance(selectedOption: String, selectedItem: String) =
            OTPVerificationFragment().apply {

                arguments = Bundle().apply {

                    putString(selectedType, selectedOption)
                    putString(mSelectedItem, selectedItem)
                }
            }

        private const val selectedType = "selectedoption"
        private const val mSelectedItem = "selecteditem"
    }

    override fun getLayoutId(): Int {
        return R.layout.o_t_p_verification_fragment
    }

    override fun getViewModel(): Class<OTPVerificationViewModel> {

        return OTPVerificationViewModel::class.java
    }

    override fun onBinding() {
        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.otp_label),
            showUpButton = true
        )

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }


        val sendOTPRequest: SendOTPRequest = SendOTPRequest(
            arguments?.getString(selectedType) ?: ""
        )
        mViewModel.callSendOTPRequest(sendOTPRequest)
        setUpViewData()
        setUpObserver()
        setUpClickListener()

    }

    private fun setUpViewData() {


        mBinding.tvCheckEmailLabel.setText(
            requireActivity().getString(R.string.otp_title) + " " + arguments?.getString(
                mSelectedItem
            ) ?: ""
        )
    }

    private fun setUpClickListener() {

        var otp: String = ""
        mBinding.otpView.setOtpCompletionListener {
            otp = it

        }

        mBinding.btnVerify.setOnClickListener {

            if (!(otp.isEmpty()) && (otp.length == 4)) {
                val verifyOTPRequest: VerifyOTPRequest = VerifyOTPRequest(otp.toInt())
                mViewModel.verfiyOTPRequest(verifyOTPRequest)
            } else {
                showMessage(requireActivity().getString(R.string.enter_valid_otp))
            }


        }

        mBinding.btnSignIn.setOnClickListener {
            replaceFragment(
                fragment = LoginFragment(),
                addToBackStack = false,
                bundle = null
            )
        }

    }

    private fun setUpObserver() {

        mViewModel.sendOTPResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {

                showMessage(it.message)
            } else {
                showMessage(it.message)
            }

        })

        mViewModel.verifyResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {

                showMessage(it.message)

                replaceFragment(
                    fragment = com.ey.hotspot.ui.login.login_fragment.LoginFragment(),
                    addToBackStack = false,
                    bundle = null
                )
            } else {
                showMessage(it.message)
            }
        })

    }


}