package com.ey.hotspot.ui.login.otpverification.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.OTPVerificationFragmentBinding
import com.ey.hotspot.ui.registration.register_user.RegisterUserFragment

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

        mBinding.otpView.setOtpCompletionListener {
            val otp: String = it

            Log.d("OTP",otp)
        }
    }


}