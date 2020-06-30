package com.ey.hotspot.ui.login.forgorpasswordmobile

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentForgotPasswordMobileBinding

class ForgotPasswordMobileFragment :
    BaseFragment<FragmentForgotPasswordMobileBinding, ForgotPasswordMobileViewModel>() {


    override fun getLayoutId(): Int {
        return R.layout.fragment_forgot_password_mobile
    }

    override fun getViewModel(): Class<ForgotPasswordMobileViewModel> {
        return ForgotPasswordMobileViewModel::class.java
    }

    override fun onBinding() {

    }

}