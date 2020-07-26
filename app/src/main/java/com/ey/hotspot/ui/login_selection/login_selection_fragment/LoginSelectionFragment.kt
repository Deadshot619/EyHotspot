package com.ey.hotspot.ui.login_selection.login_selection_fragment


import android.content.Intent
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginSelectionBinding
import com.ey.hotspot.ui.registration.RegistrationActivity
import com.ey.hotspot.ui.registration.register_user.RegisterUserFragment
import com.ey.hotspot.utils.extention_functions.replaceFragment

class LoginSelectionFragment :
    BaseFragment<FragmentLoginSelectionBinding, LoginSelectionFragmentViewModel>() {

    companion object {
        fun newInstance() = LoginSelectionFragment()
    }

    override fun getLayoutId() = R.layout.fragment_login_selection
    override fun getViewModel() = LoginSelectionFragmentViewModel::class.java
    override fun onBinding() {

        setUpListeners()

    }


    private fun setUpListeners() {
        //Login
        mBinding.btLogin.setOnClickListener {
            /*val loginIntent = Intent(activity, LoginActivity::class.java)
            startActivity(loginIntent)*/

            // replaceFragment(EmailVerificationFragment.newInstance(), true, null)

            replaceFragment(RegisterUserFragment.newInstance(), true, null)
        }

        //Registration
        mBinding.btnRegistration.setOnClickListener {
            startActivity(Intent(activity, RegistrationActivity::class.java))
        }
    }
}