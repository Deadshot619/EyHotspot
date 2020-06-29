package com.ey.hotspot.ui.registration.incomplete_registration

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentIncompleteRegistrationBinding
import com.ey.hotspot.ui.registration.email_verification.EmailVerificationFragment
import com.ey.hotspot.ui.registration.sms_verification.SmsVerificationFragment
import com.ey.hotspot.utils.replaceFragment

class IncompleteRegistrationFragment : BaseFragment<FragmentIncompleteRegistrationBinding, IncompleteRegistrationViewModel>() {

    companion object {
        fun newInstance() = IncompleteRegistrationFragment()
    }


    override fun getLayoutId() = R.layout.fragment_incomplete_registration
    override fun getViewModel() = IncompleteRegistrationViewModel::class.java
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