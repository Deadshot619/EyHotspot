package com.ey.hotspot.ui.registration.incomplete_registration

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentIncompleteRegistrationBinding

class IncompleteRegistrationFragment : BaseFragment<FragmentIncompleteRegistrationBinding, IncompleteRegistrationViewModel>() {

    companion object {
        fun newInstance() = IncompleteRegistrationFragment()
    }


    override fun getLayoutId() = R.layout.fragment_incomplete_registration
    override fun getViewModel() = IncompleteRegistrationViewModel::class.java
    override fun onBinding() {
    }
}