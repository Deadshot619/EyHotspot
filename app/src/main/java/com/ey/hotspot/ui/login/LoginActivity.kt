package com.ey.hotspot.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityLoginBinding
import com.ey.hotspot.ui.login.login_fragment.LoginFragment

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    override fun getLayoutId(): Int {

        return R.layout.activity_login
    }

    override fun getViewModel(): Class<LoginViewModel> {

        return LoginViewModel::class.java
    }

    override fun onBinding() {

        addFragment(fragment = LoginFragment(), addToBackstack = true, bundle = null)

    }

}