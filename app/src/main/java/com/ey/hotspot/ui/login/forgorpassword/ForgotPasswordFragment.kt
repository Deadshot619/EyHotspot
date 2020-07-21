package com.ey.hotspot.ui.login.forgorpassword

import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentForgotPasswordMobileBinding
import com.ey.hotspot.ui.login.verifyotp.VerifyOTPFragment
import com.ey.hotspot.utils.replaceFragment

class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordMobileBinding, ForgotPasswordViewModel>() {

    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_forgot_password_mobile
    }

    override fun getViewModel(): Class<ForgotPasswordViewModel> {
        return ForgotPasswordViewModel::class.java
    }

    override fun onBinding() {

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.forgotpassword),
            showUpButton = true
        )

        setUpViewData()
        setUpListeners()
        setUpObserver()

    }

    private fun setUpViewData() {


    }

    private fun setUpObserver() {


    }


    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {
            //Next button
            btnSendOtp.setOnClickListener {

                replaceFragment(
                    fragment = VerifyOTPFragment.newInstance(
                        inputData = mViewModel.mEmailIdOrPassword
                    ),
                    addToBackStack = true

                )
            }
        }
    }

}