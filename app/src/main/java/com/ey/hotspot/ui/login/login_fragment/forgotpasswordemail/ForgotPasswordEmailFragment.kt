package com.ey.hotspot.ui.login.login_fragment.forgotpasswordemail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ForgotPasswordEmailFragmentBinding

class ForgotPasswordEmailFragment :
    BaseFragment<ForgotPasswordEmailFragmentBinding, ForgotPasswordEmailViewModel>() {
    override fun getLayoutId(): Int {

        return R.layout.forgot_password_email_fragment
    }

    override fun getViewModel(): Class<ForgotPasswordEmailViewModel> {

        return ForgotPasswordEmailViewModel::class.java
    }

    override fun onBinding() {
    }


}