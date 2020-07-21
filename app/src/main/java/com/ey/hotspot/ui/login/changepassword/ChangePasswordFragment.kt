package com.ey.hotspot.ui.login.changepassword

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ChangePasswordFragmentBinding
import com.ey.hotspot.ui.login.login_fragment.LoginFragment
import com.ey.hotspot.ui.login.verifyotp.VerifyOTPFragment
import com.ey.hotspot.utils.replaceFragment

class ChangePasswordFragment : BaseFragment<ChangePasswordFragmentBinding,ChangePasswordViewModel>() {

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }


    override fun getLayoutId(): Int {
            return R.layout.change_password_fragment
    }

    override fun getViewModel(): Class<ChangePasswordViewModel> {

        return ChangePasswordViewModel::class.java
    }

    override fun onBinding() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.password_reset_label),
            showUpButton = true
        )
        setUpListeners()
    }

    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {

        }
    }

}