package com.ey.hotspot.ui.login.login_fragment

import android.content.Intent
import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginBinding
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.utils.constants.OptionType
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.validations.isEmailValid


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


        //replaceFragment(RegisterUserFragment.newInstance(),true,null)
        setUpListeners()
    }

    private fun setUpListeners() {
        //Submit
        mBinding.btnSignIn.setOnClickListener {

            if (validate()) {
                val homeIntent = Intent(activity, BottomNavHomeActivity::class.java)
                startActivity(homeIntent)

                mViewModel.callLogin(mViewModel.emailId, mViewModel.password)

            }

        }

        //Forgot Password
        mBinding.tvForgotPassword.setOnClickListener {
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
                    etEmailId.error = resources.getString(R.string.invalid_email)
                    false
                } else if (password.trim().isEmpty()) {
                    etPassword.error = "Enter the password"
                    false
                } else
                    true
            }
        }
    }

}