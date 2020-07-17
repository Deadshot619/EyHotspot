package com.ey.hotspot.ui.login.otpverification

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseActivity
import com.ey.hotspot.databinding.ActivityOTPVerificationBinding
import com.ey.hotspot.ui.login.otpverification.fragment.OTPVerificationViewModel

class OTPVerificationActivity : BaseActivity<ActivityOTPVerificationBinding, OTPVerificationViewModel>() {


    override fun getLayoutId(): Int {
        return R.layout.activity_o_t_p_verification
    }

    override fun getViewModel(): Class<OTPVerificationViewModel> {
        return OTPVerificationViewModel::class.java
    }

    override fun onBinding() {

    }

}