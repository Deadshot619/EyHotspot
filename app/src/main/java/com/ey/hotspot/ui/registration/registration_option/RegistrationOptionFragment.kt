package com.ey.hotspot.ui.registration.registration_option

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegistrationOptionBinding
import com.ey.hotspot.ui.login.otpverification.fragment.OTPVerificationFragment
import com.ey.hotspot.utils.constants.VerificationType
import com.ey.hotspot.utils.constants.setUpToolbar
import com.ey.hotspot.utils.extention_functions.generateCaptchaCode
import com.ey.hotspot.utils.extention_functions.replaceFragment
import com.ey.hotspot.utils.extention_functions.showMessage

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
    var selectedOption: VerificationType? = null
    lateinit var phoneNo: String
    lateinit var emailID: String

    var mCaptcha: String? = null
    var mEnteredCaptch: String? = null

    override fun getLayoutId() = R.layout.fragment_registration_option
    override fun getViewModel() = RegistrationOptionViewModel::class.java
    override fun onBinding() {

        /*setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.verify_account_label),
            showUpButton = true
        )*/
        activity?.setUpToolbar(
            mBinding.toolbarLayout,
            resources.getString(R.string.verify_account_label),
            true,
            showTextButton = false
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

        //Set Email selected
        mBinding.rbEmail.isChecked = true
    }

    private fun setUpDataViews() {

        setUpCaptcha()

    }

    private fun setUpCaptcha() {
        mCaptcha = activity?.generateCaptchaCode(5)
        mBinding.layoutCaptcha.etCaptchaText.setText(mCaptcha)
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

                    if (radio?.text.toString() == getString(R.string.sms_label)) {
                        selectedOption = VerificationType.SMS
                    }
                    if (radio?.text.toString() == getString(R.string.email_label)) {
                        selectedOption = VerificationType.EMAIL
                    }

                })


            //Submit
            btnSubmit.setOnClickListener {

                if (validate()) {
                    setUpCaptcha()
                    replaceFragment(
                        fragment = OTPVerificationFragment.newInstance(
                            selectedOption = selectedOption!!,
                            selectedItem = if (selectedOption == VerificationType.SMS) phoneNo else emailID
                        ),
                        addToBackStack = true
                    )
                }

            }
        }

        mBinding.layoutCaptcha.ivRefreshCaptchaCode.setOnClickListener {

            mCaptcha = activity?.generateCaptchaCode(5)
            mBinding.layoutCaptcha.etCaptchaText.setText(mCaptcha)

        }

    }

    private fun validate(): Boolean {
        var isValid = true
        mEnteredCaptch = mBinding.layoutCaptcha.etCaptcha.text.toString()
        mCaptcha = mBinding.layoutCaptcha.etCaptchaText.text.toString()

        if (selectedOption == null) {
            showMessage(resources.getString(R.string.choose_verify_option))
            isValid = false
        }

        if (mBinding.layoutCaptcha.etCaptcha.text?.isEmpty()!!) {
            mBinding.layoutCaptcha.etCaptcha.error = resources.getString(R.string.empty_captcha)
            isValid = false
        } else if (!(mEnteredCaptch == mCaptcha)) {
            mBinding.layoutCaptcha.etCaptcha.error = resources.getString(R.string.invalid_captcha)
            isValid = false
        }


        return isValid

    }
}
