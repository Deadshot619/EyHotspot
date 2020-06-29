package com.ey.hotspot.ui.registration.email_verification

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentEmailVerificationBinding

class EmailVerificationFragment : BaseFragment<FragmentEmailVerificationBinding, EmailVerificationViewModel>() {

    companion object {
        fun newInstance() = EmailVerificationFragment()
    }

    override fun getLayoutId() = R.layout.fragment_email_verification
    override fun getViewModel() = EmailVerificationViewModel::class.java
    override fun onBinding() {
    }
}