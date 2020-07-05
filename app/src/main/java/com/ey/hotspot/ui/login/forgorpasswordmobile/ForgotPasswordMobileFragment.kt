package com.ey.hotspot.ui.login.forgorpasswordmobile

import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentForgotPasswordMobileBinding
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.ui.registration.sms_verification.SmsVerificationFragment
import com.ey.hotspot.utils.constants.OptionType
import com.ey.hotspot.utils.replaceFragment

class ForgotPasswordMobileFragment :
    BaseFragment<FragmentForgotPasswordMobileBinding, ForgotPasswordMobileViewModel>() {

    companion object {
        fun newInstance() = ForgotPasswordMobileFragment()

    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_forgot_password_mobile
    }

    override fun getViewModel(): Class<ForgotPasswordMobileViewModel> {
        return ForgotPasswordMobileViewModel::class.java
    }

    override fun onBinding() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.forgotpassword),
            showUpButton = true
        )

        setUpListeners()
    }


    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {
            //Next button
            btnSubmit.setOnClickListener {
                replaceFragment(SmsVerificationFragment.newInstance(), true, Bundle().apply {
                    putString(
                        RegistrationOptionFragment.TYPE_KEY,
                        OptionType.TYPE_FORGOT_PASSWORD.name
                    )
                })
            }
        }
    }

}