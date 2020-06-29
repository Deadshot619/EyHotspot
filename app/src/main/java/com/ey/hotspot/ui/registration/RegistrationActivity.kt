package com.ey.hotspot.ui.registration

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityRegistrationBinding
import com.ey.hotspot.ui.registration.register_user.RegisterUserFragment

class RegistrationActivity : BaseActivity<ActivityRegistrationBinding, RegistrationActivityViewModel>() {

    override fun getLayoutId() = R.layout.activity_registration
    override fun getViewModel() = RegistrationActivityViewModel::class.java

    override fun onBinding() {
        //Add fragment to activity
        addFragment(fragment = RegisterUserFragment.newInstance(), addToBackstack = true, bundle = null)
    }
}