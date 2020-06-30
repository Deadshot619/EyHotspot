package com.ey.hotspot.ui.login.login_fragment

import android.content.Intent
import android.os.Bundle
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginBinding
import com.ey.hotspot.ui.home.HomeActivity
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.utils.constants.OptionType
import com.ey.hotspot.utils.replaceFragment


class LoginFragment :BaseFragment<FragmentLoginBinding,LoginFragmentViewModel>() {



    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun getViewModel(): Class<LoginFragmentViewModel> {
       return LoginFragmentViewModel::class.java
    }

    override fun onBinding() {


        setUpListeners()
    }

    private fun setUpListeners(){
        //Submit
        mBinding.btnSubmit.setOnClickListener {
            val homeIntent = Intent(activity,HomeActivity::class.java)
            startActivity(homeIntent)
        }

        //Forgot Password
        mBinding.btnForgotPassword.setOnClickListener {
            replaceFragment(fragment = RegistrationOptionFragment.newInstance(), addToBackStack = true, bundle = Bundle().apply {
                putString(RegistrationOptionFragment.TYPE_KEY, OptionType.TYPE_FORGOT_PASSWORD.name)
            })
        }
    }

}