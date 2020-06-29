package com.ey.hotspot.ui.login_selection.login_selection_fragment


import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginSelectionBinding

class LoginSelectionFragment : BaseFragment<FragmentLoginSelectionBinding, LoginSelectionFragmentViewModel>() {

    companion object {
        fun newInstance() = LoginSelectionFragment()
    }

    override fun getLayoutId() = R.layout.fragment_login_selection
    override fun getViewModel() = LoginSelectionFragmentViewModel::class.java
    override fun onBinding() {


    }
}