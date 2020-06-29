package com.ey.hotspot.ui.login.login_fragment.changepassword

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ChangePasswordFragmentBinding

class ChangePasswordFragment : BaseFragment<ChangePasswordFragmentBinding,ChangePasswordViewModel>() {



    override fun getLayoutId(): Int {
            return R.layout.change_password_fragment
    }

    override fun getViewModel(): Class<ChangePasswordViewModel> {

        return ChangePasswordViewModel::class.java
    }

    override fun onBinding() {
    }


}