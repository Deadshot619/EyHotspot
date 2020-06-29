package com.ey.hotspot.ui.registration.registration_option

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegistrationOptionBinding

class RegistrationOptionFragment : BaseFragment<FragmentRegistrationOptionBinding, RegistrationOptionViewModel>() {

    companion object {
        fun newInstance() = RegistrationOptionFragment()
    }

    override fun getLayoutId() = R.layout.fragment_registration_option
    override fun getViewModel() = RegistrationOptionViewModel::class.java
    override fun onBinding() {



    }

}