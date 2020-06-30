package com.ey.hotspot.ui.login.changepassword

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ChangePasswordFragmentBinding
import com.ey.hotspot.ui.login.login_fragment.LoginFragment
import com.ey.hotspot.utils.replaceFragment

class ChangePasswordFragment : BaseFragment<ChangePasswordFragmentBinding,ChangePasswordViewModel>() {



    override fun getLayoutId(): Int {
            return R.layout.change_password_fragment
    }

    override fun getViewModel(): Class<ChangePasswordViewModel> {

        return ChangePasswordViewModel::class.java
    }

    override fun onBinding() {
        setUpListeners()
    }

    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {
            //Submit button
            btnSubmit.setOnClickListener {
                replaceFragment(LoginFragment(), true, null)
            }

            //Back to login screen
            btnBackLoginScreen.setOnClickListener {
                replaceFragment(LoginFragment(), true, null)
            }
        }
    }

}