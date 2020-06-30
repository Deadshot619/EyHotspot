package com.ey.hotspot.ui.login.changepassword

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ChangePasswordFragmentBinding

class ChangePasswordFragment : BaseFragment<ChangePasswordFragmentBinding,ChangePasswordViewModel>() {



    override fun getLayoutId(): Int {
            return R.layout.change_password_fragment
    }

    override fun getViewModel(): Class<ChangePasswordViewModel> {

        return ChangePasswordViewModel::class.java
    }

    override fun onBinding() {
    }


}