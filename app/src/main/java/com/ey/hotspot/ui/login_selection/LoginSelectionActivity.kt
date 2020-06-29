package com.ey.hotspot.ui.login_selection

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityLoginSelectionBinding
import com.ey.hotspot.ui.login_selection.login_selection_fragment.LoginSelectionFragment

class LoginSelectionActivity :
    BaseActivity<ActivityLoginSelectionBinding, LoginSelectionViewModel>() {

    override fun getLayoutId() = R.layout.activity_login_selection
    override fun getViewModel() = LoginSelectionViewModel::class.java

    override fun onBinding() {
        //Add fragment to activity
        addFragment(fragment = LoginSelectionFragment.newInstance(), addToBackstack = true, bundle = null)
    }
}