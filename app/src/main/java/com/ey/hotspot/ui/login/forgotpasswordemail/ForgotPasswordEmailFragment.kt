package com.ey.hotspot.ui.login.forgotpasswordemail

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ForgotPasswordEmailFragmentBinding

class ForgotPasswordEmailFragment :
    BaseFragment<ForgotPasswordEmailFragmentBinding, ForgotPasswordEmailViewModel>() {

    override fun getLayoutId() = R.layout.forgot_password_email_fragment
    override fun getViewModel() = ForgotPasswordEmailViewModel::class.java

    override fun onBinding() {

    }
}