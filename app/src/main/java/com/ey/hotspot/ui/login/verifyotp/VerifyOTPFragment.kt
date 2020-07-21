package com.ey.hotspot.ui.login.verifyotp

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.VerifyOtpFragmentBinding
import com.ey.hotspot.ui.login.changepassword.ChangePasswordFragment
import com.ey.hotspot.ui.login.otpverification.fragment.OTPVerificationFragment
import com.ey.hotspot.ui.login.otpverification.fragment.model.VerifyOTPRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordVerifyOTPRequest
import com.ey.hotspot.ui.registration.email_verification.EmailVerificationFragment
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage

class VerifyOTPFragment :
    BaseFragment<VerifyOtpFragmentBinding, VerifyOTPViewModel>() {


    companion object {
        fun newInstance(inputData: String) = VerifyOTPFragment().apply {
            arguments = Bundle().apply {
                putString(mInputData, inputData)
            }
        }

        public const val mInputData = "inputData"


    }

    override fun getLayoutId() = R.layout.verify_otp_fragment
    override fun getViewModel() = VerifyOTPViewModel::class.java

    override fun onBinding() {

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.verify_account),
            showUpButton = true
        )

        setUpDataView()
        setUpListener()
        setUpObserver()


    }

    private fun setUpDataView() {

        val forgotPasswordRequest: ForgotPasswordRequest =
            ForgotPasswordRequest(arguments?.getString(mInputData) ?: "")

        mViewModel.callForgotPasswordAPI(forgotPasswordRequest)

    }

    private fun setUpListener() {

        var otp: String = ""
        mBinding.otpView.setOtpCompletionListener {
            otp = it

        }

        mBinding.btnVerify.setOnClickListener {

            if (!(otp.isEmpty()) && (otp.length == 4)) {
                val verifyOTPRequest: ForgotPasswordVerifyOTPRequest =
                    ForgotPasswordVerifyOTPRequest(otp.toInt())
                mViewModel.verifyForgotPasswordOTP(verifyOTPRequest)
            } else {
                showMessage(requireActivity().getString(R.string.enter_valid_otp))
            }


        }



        mBinding.btResendOTP.setOnClickListener {

        }

    }

    private fun setUpObserver() {

        mViewModel.forgotPasswordResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {
                showMessage(it.message, true)
            } else {
                showMessage(it.message, true)
            }
        })


        mViewModel.forgotPasswordVerifyOTPResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {

                showMessage(it.message, true)
            } else {
                showMessage(it.message, true)
            }

        })
    }


}