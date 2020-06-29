package com.ey.hotspot.ui.login_selection.login_selection_fragment


import android.content.Intent
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginSelectionBinding
import com.ey.hotspot.ui.login.LoginActivity

class LoginSelectionFragment :
    BaseFragment<FragmentLoginSelectionBinding, LoginSelectionFragmentViewModel>() {

    override fun getLayoutId() = R.layout.fragment_login_selection
    override fun getViewModel() = LoginSelectionFragmentViewModel::class.java
    override fun onBinding() {

        mBinding.btLogin.setOnClickListener {

            val loginIntent = Intent(activity, LoginActivity::class.java)
            startActivity(loginIntent)
        }


    }

}