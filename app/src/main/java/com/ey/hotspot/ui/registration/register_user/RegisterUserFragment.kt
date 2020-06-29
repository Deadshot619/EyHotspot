package com.ey.hotspot.ui.registration.register_user

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegisterUserBinding

class RegisterUserFragment : BaseFragment<FragmentRegisterUserBinding, RegisterUserViewModel>() {

    override fun getLayoutId() = R.layout.fragment_register_user
    override fun getViewModel() = RegisterUserViewModel::class.java

    override fun onBinding() {



    }

}