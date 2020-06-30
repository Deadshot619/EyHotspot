package com.ey.hotspot.ui.registration.register_user

import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegisterUserBinding
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.utils.constants.OptionType
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.validations.isEmailValid
import com.ey.hotspot.utils.validations.isPasswordValid
import com.ey.hotspot.utils.validations.isValidMobile

class RegisterUserFragment : BaseFragment<FragmentRegisterUserBinding, RegisterUserViewModel>() {

    companion object {
        fun newInstance() = RegisterUserFragment()
    }

    override fun getLayoutId() = R.layout.fragment_register_user
    override fun getViewModel() = RegisterUserViewModel::class.java

    override fun onBinding() {
        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpListeners()
    }

    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {
            //Next button
            btnNext.setOnClickListener {
                if (validate())
                    replaceFragment(RegistrationOptionFragment.newInstance(), true, Bundle().apply {
                        putString(RegistrationOptionFragment.TYPE_KEY, OptionType.TYPE_REGISTRATION.name)
                    })
            }

            //Sign In button
            btnSignIn.setOnClickListener {
                replaceFragment(RegistrationOptionFragment.newInstance(), true, Bundle().apply {
                    putString(RegistrationOptionFragment.TYPE_KEY, OptionType.TYPE_REGISTRATION.name)
                })
            }
        }
    }

    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        mViewModel.run {
            mBinding.run {
                return if (firstName.trim().isEmpty()) {
                    edtFirstName.error = "Invalid First Name"
                    false
                } else if (lastName.trim().isEmpty()) {
                    edtLastName.error = "Invalid Last Name"
                    false
                } else if (!emailId.isEmailValid()) {
                    edtEmail.error = "Invalid Email Id"
                    false
                } else if (!mobileNumber.isValidMobile()) {
                    edtMobileNo.error = "Invalid Mobile Number"
                    false
                } else if (password.trim().isEmpty()) {
                    edtPassword.error = "Invalid Password"
                    false
                } else if (confirmPassword.trim().isEmpty()) {
                    edtConfirmPassword.error = "Passwords do not match"
                    false
                } else if (!password.isPasswordValid(confirmPassword)) {
                    edtPassword.error = "Passwords do not match"
                    edtConfirmPassword.error = "Passwords do not match"
                    false
                } else true
            }
        }
    }
}