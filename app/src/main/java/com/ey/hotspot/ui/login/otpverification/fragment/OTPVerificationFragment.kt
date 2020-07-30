package com.ey.hotspot.ui.login.otpverification.fragment

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.OTPVerificationFragmentBinding
import com.ey.hotspot.network.request.SendOTPRequest
import com.ey.hotspot.network.request.VerifyOTPRequest
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.utils.extention_functions.replaceFragment
import com.ey.hotspot.utils.extention_functions.showMessage
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
            if (otp.isNotEmpty() && (otp.length == 5)) {
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

        mBinding.tvResendOTP.setOnClickListener {
            val sendOTPRequest: SendOTPRequest = SendOTPRequest(
                arguments?.getString(selectedType) ?: ""
            )
            mViewModel.callSendOTPRequest(sendOTPRequest)

        }

    }

    private fun setUpObserver() {

        mViewModel.sendOTPResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {

                showMessage(it.message)

                mBinding.tvDisplayOTP.setText( it.message)


            } else {
                showMessage(it.message)
            }

        })

        mViewModel.verifyResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                showMessage(it.message)
                goToHomeScreen()
            } else {
                showMessage(it.message)
            }
        })

    }


    fun goToHomeScreen(){
        startActivity(Intent(activity, BottomNavHomeActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        activity?.finish()
    }

}