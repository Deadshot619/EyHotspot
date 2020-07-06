package com.ey.hotspot.ui.registration.registration_option

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegistrationOptionBinding
import com.ey.hotspot.ui.login.forgorpasswordmobile.ForgotPasswordMobileFragment
import com.ey.hotspot.ui.login.forgotpasswordemail.ForgotPasswordEmailFragment
import com.ey.hotspot.ui.login.login_fragment.LoginFragment
import com.ey.hotspot.ui.registration.email_verification.EmailVerificationFragment
import com.ey.hotspot.ui.registration.sms_verification.SmsVerificationFragment
import com.ey.hotspot.utils.constants.OptionType
import com.ey.hotspot.utils.replaceFragment

class RegistrationOptionFragment :
    BaseFragment<FragmentRegistrationOptionBinding, RegistrationOptionViewModel>() {

    companion object {
        fun newInstance() = RegistrationOptionFragment()

        const val TYPE_KEY = "type_key"
    }

    lateinit var TYPE_VALUE: String

    override fun getLayoutId() = R.layout.fragment_registration_option
    override fun getViewModel() = RegistrationOptionViewModel::class.java
    override fun onBinding() {
        TYPE_VALUE = getDataFromArguments(this, TYPE_KEY)
//        if (TYPE_VALUE == OptionType.TYPE_REGISTRATION.name)
//            else

        setUpListeners()
    }


    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {
            //Next button
            btnOtpSms.setOnClickListener {
                if (TYPE_VALUE == OptionType.TYPE_REGISTRATION.name)
                    replaceFragment(SmsVerificationFragment.newInstance(), true, null)
                else
                    replaceFragment(ForgotPasswordMobileFragment(), true, null)
            }

            //Sign In button
            btnEmailLink.setOnClickListener {
                if (TYPE_VALUE == OptionType.TYPE_REGISTRATION.name)
                    replaceFragment(EmailVerificationFragment.newInstance(), true, null)
                else
                    replaceFragment(ForgotPasswordEmailFragment(), true, null)

            }

            btnSignIn.setOnClickListener {

                replaceFragment(LoginFragment.newInstance(), false, null)
            }


        }
    }
}