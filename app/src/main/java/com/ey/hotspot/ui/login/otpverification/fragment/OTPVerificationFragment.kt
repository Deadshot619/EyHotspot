package com.ey.hotspot.ui.login.otpverification.fragment

import android.util.Log
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.OTPVerificationFragmentBinding

class OTPVerificationFragment :
    BaseFragment<OTPVerificationFragmentBinding, OTPVerificationViewModel>() {



    companion object {
        fun newInstance() = OTPVerificationFragment()
    }
    override fun getLayoutId(): Int {
        return R.layout.o_t_p_verification_fragment
    }

    override fun getViewModel(): Class<OTPVerificationViewModel> {
        return OTPVerificationViewModel::class.java
    }

    override fun onBinding() {

        setUpToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.otp_label), showUpButton = true)

        mBinding.otpView.setOtpCompletionListener {
            val otp: String = it

            Log.d("OTP",otp)
        }
    }


}