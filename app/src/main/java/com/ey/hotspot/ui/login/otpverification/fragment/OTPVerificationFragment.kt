package com.ey.hotspot.ui.login.otpverification.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.OTPVerificationFragmentBinding
import com.ey.hotspot.network.request.SendOTPRequest
import com.ey.hotspot.network.request.VerifyOTPRequest
import com.ey.hotspot.utils.constants.VerificationType
import com.ey.hotspot.utils.constants.setUpToolbar
import com.ey.hotspot.utils.extention_functions.goToHomeScreen
import com.ey.hotspot.utils.extention_functions.replaceFragment
import com.ey.hotspot.utils.extention_functions.showMessage
import com.facebook.login.LoginFragment

class OTPVerificationFragment :
    BaseFragment<OTPVerificationFragmentBinding, OTPVerificationViewModel>() {


    companion object {
        fun newInstance(selectedOption: VerificationType, selectedItem: String, callOtpApi: Boolean = true) =
            OTPVerificationFragment().apply {

                arguments = Bundle().apply {
                    putString(selectedType, selectedOption.value)
                    putString(mSelectedItem, selectedItem)
                    putBoolean(CALL_OTP_API, callOtpApi)
                }
            }

        private const val selectedType = "selected_type"
        private const val mSelectedItem = "selected_item"
        private const val CALL_OTP_API = "call_otp_api"
    }

    override fun getLayoutId(): Int {
        return R.layout.o_t_p_verification_fragment
    }

    override fun getViewModel(): Class<OTPVerificationViewModel> {

        return OTPVerificationViewModel::class.java
    }

    override fun onBinding() {
        /*setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.otp_label),
            showUpButton = true
        )*/

        activity?.setUpToolbar(
            mBinding.toolbarLayout,
            resources.getString(R.string.otp_label),
            true,
            showTextButton = false
        )

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }


        val sendOTPRequest: SendOTPRequest = SendOTPRequest(
            arguments?.getString(selectedType) ?: ""
        )

        //Call the APi, only if its true
        if (arguments?.getBoolean(CALL_OTP_API)!!)
            mViewModel.callSendOTPRequest(sendOTPRequest)

        setUpViewData()
        setUpObserver()
        setUpClickListener()

    }

    private fun setUpViewData() {
       /* mBinding.tvCheckEmailLabel.text =
            requireActivity().getString(R.string.otp_title) + " " + arguments?.getString(
                mSelectedItem
            ) ?: ""*/

        //Change image according to Selected Type
        if ( arguments?.getString(selectedType) == VerificationType.SMS.value)
            mBinding.ivCheck.setImageResource(R.drawable.ic_email_verification)
        else
            mBinding.ivCheck.setImageResource(R.drawable.ic_email_verification)

    }

    private fun setUpClickListener() {
        //Verify
        mBinding.btnVerify.setOnClickListener {
            val otp = mBinding.otpView.text.toString()

            if (otp.isNotEmpty() && (otp.length == 5)) {
                val verifyOTPRequest: VerifyOTPRequest = VerifyOTPRequest(otp.toInt())
                mViewModel.verfiyOTPRequest(verifyOTPRequest)
            } else {
                showMessage(requireActivity().getString(R.string.invalid_otp_label))
            }

        }

        mBinding.btnSignIn.setOnClickListener {
            replaceFragment(
                fragment = LoginFragment(),
                addToBackStack = false,
                bundle = null
            )
        }

        //Resend Otp
        mBinding.tvResendOTP.setOnClickListener {
            val sendOTPRequest: SendOTPRequest = SendOTPRequest(
                arguments?.getString(selectedType) ?: ""
            )
            mViewModel.callSendOTPRequest(sendOTPRequest)
        }
    }

    private fun setUpObserver() {

        mViewModel.sendOTPResponse.observe(viewLifecycleOwner, Observer {
            showMessage(it.message)
        })

        mViewModel.verifyResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                showMessage(it.message)
                requireActivity().goToHomeScreen()
            } else {
                showMessage(it.message)
            }
        })

    }
}