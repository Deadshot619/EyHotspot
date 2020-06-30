package com.ey.hotspot.ui.registration.sms_verification

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentSmsVerificationBinding
import com.ey.hotspot.ui.login.changepassword.ChangePasswordFragment
import com.ey.hotspot.utils.replaceFragment

class SmsVerificationFragment : BaseFragment<FragmentSmsVerificationBinding, SmsVerificationViewModel>() {

    companion object {
        fun newInstance() = SmsVerificationFragment()

        const val TYPE_KEY = "type_key"
    }

    lateinit var TYPE_VALUE: String


    override fun getLayoutId() = R.layout.fragment_sms_verification
    override fun getViewModel() = SmsVerificationViewModel::class.java
    override fun onBinding() {
        TYPE_VALUE = getDataFromArguments(this, TYPE_KEY)

        setUpListeners()
    }

    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {
            //Next button
            btnSubmit.setOnClickListener {
//                if (TYPE_VALUE == OptionType.TYPE_REGISTRATION.name)
                    replaceFragment(ChangePasswordFragment(), true, null)
//                else
//                    replaceFragment(ForgotPasswordMobileFragment(), true, null)
            }
        }
    }


}