package com.ey.hotspot.ui.registration.forgot_password

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentForgotPasswordMobileBinding

class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordMobileBinding, ForgotPasswordViewModel>() {


    override fun getLayoutId(): Int {

        return R.layout.forgot_password_fragment
    }

    override fun getViewModel(): Class<ForgotPasswordViewModel> {

        return ForgotPasswordViewModel::class.java
    }

    override fun onBinding() {

        
    }


}