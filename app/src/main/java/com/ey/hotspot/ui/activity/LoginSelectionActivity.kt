package com.ey.hotspot.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ActivityLoginSelectionBinding
import com.ey.hotspot.viewmodels.LoginSelectionViewModel
import com.ey.stringlocalization.utils.Constants
import com.ey.stringlocalization.utils.LanguageManager

class LoginSelectionActivity :
    BaseActivity<ActivityLoginSelectionBinding, LoginSelectionViewModel>() {



    override fun getLayoutId(): Int {
        return R.layout.activity_login_selection
    }

    override fun getViewModel(): Class<LoginSelectionViewModel> {
        return LoginSelectionViewModel::class.java
    }

    override fun onBinding() {

        mBinding.btLogin.setOnClickListener {

        }
    }
}