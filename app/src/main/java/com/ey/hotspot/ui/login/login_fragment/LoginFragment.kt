package com.ey.hotspot.ui.login.login_fragment

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginBinding
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.login.login_fragment.model.LoginRequest
import com.ey.hotspot.ui.registration.register_user.RegisterUserFragment
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.utils.constants.OptionType
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage
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
        setUpListeners()
        setUpObservers()
    }

    private fun setUpObservers() {


        mViewModel.loginResponse.observe(viewLifecycleOwner, Observer {

            val homeIntent = Intent(activity, BottomNavHomeActivity::class.java)
            startActivity(homeIntent)

        })

        mViewModel.errorText.observe(viewLifecycleOwner, Observer {

            showMessage(it, false)
        })
    }

    private fun setUpListeners() {
        //Submit
        mBinding.btnSignIn.setOnClickListener {

            if (validate()) {
                val homeIntent = Intent(activity, BottomNavHomeActivity::class.java)
                startActivity(homeIntent)
                val loginRequest: LoginRequest =
                    LoginRequest(mViewModel.emailId, mViewModel.password)
                mViewModel.callLogin(loginRequest)

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

        //Register New user
        mBinding.tvGetStarted.setOnClickListener {

            replaceFragment(
                fragment = RegisterUserFragment.newInstance(),
                addToBackStack = true,
                bundle = null
            )
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
                    etPassword.error = resources.getString(R.string.enter_password)
                    false
                } else
                    true
            }
        }
    }

}