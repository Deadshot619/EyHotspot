package com.ey.hotspot.ui.login.forgotpasswordemail

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ForgotPasswordEmailFragmentBinding
import com.ey.hotspot.ui.registration.email_verification.EmailVerificationFragment
import com.ey.hotspot.utils.replaceFragment

class ForgotPasswordEmailFragment :
    BaseFragment<ForgotPasswordEmailFragmentBinding, ForgotPasswordEmailViewModel>() {

    override fun getLayoutId() = R.layout.forgot_password_email_fragment
    override fun getViewModel() = ForgotPasswordEmailViewModel::class.java

    override fun onBinding() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.forgotpassword),
            showUpButton = true
        )

        mBinding.btnSubmit.setOnClickListener {

            replaceFragment(fragment = EmailVerificationFragment.newInstance(),addToBackStack = true,bundle = null)
        }
    }
}