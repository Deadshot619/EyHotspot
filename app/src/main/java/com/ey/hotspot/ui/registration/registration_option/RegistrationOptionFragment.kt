package com.ey.hotspot.ui.registration.registration_option

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegistrationOptionBinding
import com.ey.hotspot.ui.login.otpverification.fragment.OTPVerificationFragment
import com.ey.hotspot.utils.generateCaptchaCode
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage

class RegistrationOptionFragment :
    BaseFragment<FragmentRegistrationOptionBinding, RegistrationOptionViewModel>() {

    companion object {
        fun newInstance(emailID: String, phoneNo: String) = RegistrationOptionFragment().apply {
            arguments = Bundle().apply {
                putString(mEmailId, emailID)
                putString(mPhoneNO, phoneNo)
            }
        }

        const val TYPE_KEY = "type_key"
        const val mEmailId = "emailid"
        const val mPhoneNO = "phone_no"

        const val SMS = "sms"
        const val EMAIL = "email"
    }

    lateinit var TYPE_VALUE: String
    var selectedOption: String = ""
    lateinit var phoneNo: String
    lateinit var emailID: String

    var mCaptcha: String? = null


    override fun getLayoutId() = R.layout.fragment_registration_option
    override fun getViewModel() = RegistrationOptionViewModel::class.java
    override fun onBinding() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.registration_label),
            showUpButton = true
        )
        TYPE_VALUE = getDataFromArguments(this, TYPE_KEY)
        phoneNo = arguments?.getString(mPhoneNO)?.trim() ?: ""
        emailID = arguments?.getString(mEmailId)?.trim() ?: ""

//        if (TYPE_VALUE == OptionType.TYPE_REGISTRATION.name)
//            else

        setUpDataViews()

        setUpListeners()


        //If phone no. id empty, then hide the selection
        if (phoneNo.isEmpty())
            mBinding.rbSms.visibility = View.GONE
    }

    private fun setUpDataViews() {

        setUpCaptcha()

    }

    private fun setUpCaptcha() {
        mCaptcha = activity?.generateCaptchaCode(4)
        mBinding.etCaptchaText.setText(mCaptcha)
    }


    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {

            //Radio button
            rgSelectedMethod.setOnCheckedChangeListener(
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    val radio: RadioButton? = group?.findViewById(checkedId)

                    if (radio?.text.toString() == "SMS") {
                        selectedOption = SMS
                    }
                    if (radio?.text.toString() == "Email") {
                        selectedOption = EMAIL
                    }

                })


            //Submit
            btnSubmit.setOnClickListener {


                if (validate()) {
                    replaceFragment(
                        fragment = OTPVerificationFragment.newInstance(
                            selectedOption = selectedOption,
                            selectedItem = if (selectedOption == SMS) phoneNo else emailID
                        ),
                        addToBackStack = true
                    )
                }

            }
        }

    }

    private fun validate(): Boolean {
        var isValid = true
        if (selectedOption.trim().isEmpty()) {
            showMessage(resources.getString(R.string.choose_verify_option))
            isValid = false
        }

      /*  if (mBinding.etCaptcha.text?.isEmpty()!!) {
            mBinding.etCaptcha.error = resources.getString(R.string.enter_captcha)
            isValid = false
        }

        if (!mBinding.etCaptcha.text?.equals(mCaptcha)!!) {
            mBinding.etCaptcha.error = resources.getString(R.string.invalid_captcha)
            isValid = false
        }*/

        return isValid

    }
}
