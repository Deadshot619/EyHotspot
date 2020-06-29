package com.ey.hotspot.ui.registration.register_user

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegisterUserBinding
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.utils.replaceFragment

class RegisterUserFragment : BaseFragment<FragmentRegisterUserBinding, RegisterUserViewModel>() {

    companion object {
        fun newInstance() = RegisterUserFragment()
    }

    override fun getLayoutId() = R.layout.fragment_register_user
    override fun getViewModel() = RegisterUserViewModel::class.java

    override fun onBinding() {
        setUpListeners()
    }

    fun setUpListeners(){
        mBinding.run {
            btnNext.setOnClickListener{
                replaceFragment(RegistrationOptionFragment.newInstance(), true, null)
            }

            btnSignIn.setOnClickListener{
                replaceFragment(RegistrationOptionFragment.newInstance(), true, null)
            }
        }
    }
}