package com.ey.hotspot.ui.login.changepassword

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ChangePasswordFragmentBinding
import com.ey.hotspot.network.request.ResetPasswordRequest
import com.ey.hotspot.network.response.ResetPasswordResponse
import com.ey.hotspot.ui.login.login_fragment.LoginFragment
import com.ey.hotspot.utils.dialogs.OkDialog
import com.ey.hotspot.utils.extention_functions.replaceFragment
import com.ey.hotspot.utils.extention_functions.showMessage
import com.ey.hotspot.utils.validations.isEmailValid
import com.ey.hotspot.utils.validations.isValidPassword
import com.google.gson.Gson
import com.google.gson.JsonObject

class ChangePasswordFragment :
    BaseFragment<ChangePasswordFragmentBinding, ChangePasswordViewModel>() {


    //Create 'OK' Dialog
    val dialog by lazy {
        OkDialog(requireContext()).apply {
            setViews(
                title = "",
                okBtn = {
                    this.dismiss()
                }
            )
        }
    }

    companion object {
        fun newInstance(otp: String) = ChangePasswordFragment().apply {

            arguments = Bundle().apply {

                putString(mOTP, otp)
            }
        }

        public const val mOTP = "mOTP"
    }


    override fun getLayoutId(): Int {
        return R.layout.change_password_fragment
    }

    override fun getViewModel(): Class<ChangePasswordViewModel> {

        return ChangePasswordViewModel::class.java
    }

    override fun onBinding() {
        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.password_reset_label),
            showUpButton = true
        )
        setUpDataViews()
        setUpListeners()
        setUpObserver()
    }

    private fun setUpDataViews() {


        mViewModel.emailId = HotSpotApp.prefs!!.getForgotPasswordField()

    }

    private fun setUpObserver() {
        mViewModel.resetPassworResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {
                replaceFragment(
                    fragment = LoginFragment.newInstance(goToVerificationFragment = false),
                    addToBackStack = true,
                    bundle = null
                )
                showMessage(it.message, true)
            } else {
                try {
                    dialog.setViews(
                        fetchErrorResponse(it.data).toString()
                        , okBtn = {
                            dialog.dismiss()
                        }
                    )
                    dialog.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun fetchErrorResponse(resetPasswordResponse: ResetPasswordResponse): StringBuilder {
        val stringBuilder = StringBuilder()
        val jsonStrinData = Gson().toJson(resetPasswordResponse)
        val convertedObject: JsonObject =
            Gson().fromJson(jsonStrinData, JsonObject::class.java)
        if (convertedObject.has("token")) {
            stringBuilder.append(convertedObject.getAsJsonArray("token").get(0))
            stringBuilder.append("\n");
        }
        if (convertedObject.has("password_confirmation")) {
            stringBuilder.append(convertedObject.getAsJsonArray("password_confirmation").get(0))
            stringBuilder.append("\n");

        }

        if (convertedObject.has("email")) {
            stringBuilder.append(convertedObject.getAsJsonArray("email").get(0))
            stringBuilder.append("\n");

        }
        if (convertedObject.has("password")) {
            stringBuilder.append(convertedObject.getAsJsonArray("password").get(0))

        }

        if (convertedObject.has("otp")) {
            stringBuilder.append(convertedObject.getAsJsonArray("otp").get(0))

        }

        return stringBuilder;

    }

    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {

            btnSubmit.setOnClickListener {

                if (validate2()) {
                    val resetPasswordRequest: ResetPasswordRequest = ResetPasswordRequest(
                        mViewModel.emailId,
                        mViewModel.password,
                        mViewModel.confirmPassword,
                        HotSpotApp.prefs!!.getRegistrationTempToken(),
                        arguments?.getString(
                            mOTP
                        ) ?: ""
                    )

                    mViewModel.callResetPassworAPI(resetPasswordRequest)
                }
            }
        }
    }


    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        var emailId: Boolean = false
        var password: Boolean = false
        var confirmPassword: Boolean = false
        var checkPasswordConfirmPassword = false

        if (!mViewModel.emailId!!.isEmailValid()) {
            mBinding.edtEmail.error = resources.getString(R.string.invalid_email_label)
            emailId = false
        } else {
            emailId = true
        }

        if (mViewModel.password.trim().isEmpty()) {
            mBinding.edtPassword.error = resources.getString(R.string.invalid_password)
            password = false
        } else {

            password = true
        }
        if (mViewModel.confirmPassword.trim().isEmpty()) {
            mBinding.edtConfirmPassword.error =
                resources.getString(R.string.invalid_confirm_password)
            confirmPassword = false
        } else {
            confirmPassword = true
        }

        if (mViewModel.password.equals(mViewModel.confirmPassword)) {

            if ((mViewModel.password.isEmpty() && mViewModel.confirmPassword.isEmpty())) {
                mBinding.edtPassword.error = resources.getString(R.string.pwd_confirm_pwd_empty)
                mBinding.edtConfirmPassword.error =
                    resources.getString(R.string.pwd_confirm_pwd_empty)
                checkPasswordConfirmPassword = false
            } else {
                checkPasswordConfirmPassword = true
                mBinding.edtPassword.error = null
                mBinding.edtConfirmPassword.error = null
            }

        } else {
            mBinding.edtPassword.error = resources.getString(R.string.pwd_not_match)
            mBinding.edtConfirmPassword.error = resources.getString(R.string.pwd_not_match)
            checkPasswordConfirmPassword = false
        }
        if (emailId == true &&
            password == true && confirmPassword == true &&
            checkPasswordConfirmPassword == true
        ) {
            return true
        } else {
            return false
        }
    }

    /**
     * Method to validate input fields
     */
    private fun validate2(): Boolean {
        var isValid = true

        mViewModel.run {
            mBinding.run {

                if (!emailId!!.isEmailValid()) {
                    edtEmail.error = resources.getString(R.string.invalid_email_label)
                    isValid = false
                }

                if (!password.trim().isValidPassword()) {
                    edtPassword.error = resources.getString(R.string.password_format)
                    edtConfirmPassword.error = resources.getString(R.string.password_format)
                    isValid = false
                } else if (password != confirmPassword) {
                    edtPassword.error = resources.getString(R.string.pwd_not_match)
                    edtConfirmPassword.error = resources.getString(R.string.pwd_not_match)
                    isValid = false
                }
            }
        } ?: return false

        return isValid
    }



}