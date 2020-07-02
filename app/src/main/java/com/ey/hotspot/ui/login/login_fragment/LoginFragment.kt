package com.ey.hotspot.ui.login.login_fragment

import android.content.Intent
import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginBinding
import com.ey.hotspot.ui.home.HomeActivity
import com.ey.hotspot.ui.registration.register_user.RegisterUserFragment
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.utils.constants.OptionType
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.validations.isEmailValid
import com.ey.hotspot.utils.validations.isPasswordValid
import com.ey.hotspot.utils.validations.isValidMobile


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginFragmentViewModel>() {


    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun getViewModel(): Class<LoginFragmentViewModel> {
        return LoginFragmentViewModel::class.java
    }

    override fun onBinding() {


        mBinding.run {
           lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }
        /*   val homeIntent = Intent(activity,HomeActivity::class.java)
           startActivity(homeIntent)*/


       replaceFragment(RegisterUserFragment.newInstance(),true,null)
        //setUpListeners()
    }

    private fun setUpListeners() {
        //Submit
        mBinding.btnSubmit.setOnClickListener {

            if (validate()) {
                val homeIntent = Intent(activity, HomeActivity::class.java)
                startActivity(homeIntent)

            }

        }

        //Forgot Password
        mBinding.btnForgotPassword.setOnClickListener {
            replaceFragment(
                fragment = RegistrationOptionFragment.newInstance(),
                addToBackStack = true,
                bundle = Bundle().apply {
                    putString(
                        RegistrationOptionFragment.TYPE_KEY,
                        OptionType.TYPE_FORGOT_PASSWORD.name
                    )
                })
        }
    }


    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        mViewModel.run {
            mBinding.run {
                return if (!emailId.isEmailValid()) {
                    etLoginEmailId.error = resources.getString(R.string.invalid_email)
                    false
                } else if (password.trim().isEmpty()) {
                    etLoginPassword.error = "Enter the password"
                    false
                } else
                    true
            }
        }
    }

}