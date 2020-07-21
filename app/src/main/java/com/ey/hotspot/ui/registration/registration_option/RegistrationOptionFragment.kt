package com.ey.hotspot.ui.registration.registration_option

import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegistrationOptionBinding
import com.ey.hotspot.ui.login.login_fragment.LoginFragment
import com.ey.hotspot.ui.login.otpverification.fragment.OTPVerificationFragment
import com.ey.hotspot.utils.replaceFragment

class RegistrationOptionFragment :
    BaseFragment<FragmentRegistrationOptionBinding, RegistrationOptionViewModel>() {

    companion object {
        fun newInstance(emailID: String, phoneNo: String) = RegistrationOptionFragment().apply {
            arguments = Bundle().apply {

                putString(mEmailId, emailID)
                putString(mPhoneNO, phoneNo)
            }
        }

        const val TYPE_KEY = "type_key"
        const val mEmailId = "emailid"
        const val mPhoneNO = "phone_no"
    }

    lateinit var TYPE_VALUE: String

    override fun getLayoutId() = R.layout.fragment_registration_option
    override fun getViewModel() = RegistrationOptionViewModel::class.java
    override fun onBinding() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.registration_label),
            showUpButton = true
        )
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
                replaceFragment(
                    fragment = OTPVerificationFragment.newInstance(
                        selectedOption = "sms",
                        selectedItem = arguments?.getString(mPhoneNO) ?: ""
                    ),
                    addToBackStack = true
                )
            }

            //Sign In button
            btnEmailLink.setOnClickListener {

                replaceFragment(
                    fragment = OTPVerificationFragment.newInstance(
                        selectedOption = "email",
                        selectedItem = arguments?.getString(mEmailId) ?: ""
                    ),
                    addToBackStack = true
                )
            }

            btnSignIn.setOnClickListener {
                replaceFragment(LoginFragment.newInstance(), false, null)
            }
        }
    }
}