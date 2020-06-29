package com.ey.hotspot.ui.registration.registration_option

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegistrationOptionBinding
import com.ey.hotspot.ui.registration.email_verification.EmailVerificationFragment
import com.ey.hotspot.ui.registration.sms_verification.SmsVerificationFragment
import com.ey.hotspot.utils.replaceFragment

class RegistrationOptionFragment : BaseFragment<FragmentRegistrationOptionBinding, RegistrationOptionViewModel>() {

    companion object {
        fun newInstance() = RegistrationOptionFragment()
    }

    override fun getLayoutId() = R.layout.fragment_registration_option
    override fun getViewModel() = RegistrationOptionViewModel::class.java
    override fun onBinding() {

        setUpListeners()
    }


    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {
            //Next button
            btnOtpSms.setOnClickListener {
                replaceFragment(SmsVerificationFragment.newInstance(), true, null)
            }

            //Sign In button
            btnEmailLink.setOnClickListener {
                replaceFragment(EmailVerificationFragment.newInstance(), true, null)
            }
        }
    }
}