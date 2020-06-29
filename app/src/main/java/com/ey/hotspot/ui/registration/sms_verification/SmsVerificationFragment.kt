package com.ey.hotspot.ui.registration.sms_verification

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentSmsVerificationBinding

class SmsVerificationFragment : BaseFragment<FragmentSmsVerificationBinding, SmsVerificationViewModel>() {

    companion object {
        fun newInstance() = SmsVerificationFragment()
    }

    override fun getLayoutId() = R.layout.fragment_sms_verification
    override fun getViewModel() = SmsVerificationViewModel::class.java
    override fun onBinding() {
    }
}