package com.ey.hotspot.ui.login

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityLoginBinding
import com.ey.hotspot.ui.login.login_fragment.LoginFragment
import com.ey.hotspot.utils.calculateHashKey

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override fun getLayoutId(): Int {

        return R.layout.activity_login
    }

    override fun getViewModel(): Class<LoginViewModel> {

        return LoginViewModel::class.java
    }

    override fun onBinding() {

        supportActionBar?.title = getString(R.string.login_button)
        calculateHashKey("com.ey.hotspot.ey_hotspot")

        replaceFragment(fragment = LoginFragment(), addToBackstack = false, bundle = null)

    }
}