package com.ey.hotspot.ui.login.forgorpassword

import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentForgotPasswordMobileBinding
import com.ey.hotspot.ui.login.verifyotp.VerifyOTPFragment
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordResponse
import com.ey.hotspot.utils.dialogs.OkDialog
import com.ey.hotspot.utils.extention_functions.replaceFragment
import com.ey.hotspot.utils.extention_functions.showMessage
import com.google.gson.Gson
import com.google.gson.JsonObject

class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordMobileBinding, ForgotPasswordViewModel>() {

    //Create 'OK' Dialog
    val dialog by lazy {
        OkDialog(requireContext()).apply {
            setViews(
                okBtn = {
                    this.dismiss()
                }
            )
        }
    }

    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_forgot_password_mobile
    }

    override fun getViewModel(): Class<ForgotPasswordViewModel> {
        return ForgotPasswordViewModel::class.java
    }

    override fun onBinding() {

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.forgotpassword_label),
            showUpButton = true
        )

        setUpViewData()
        setUpListeners()
        setUpObserver()

    }

    private fun setUpViewData() {


    }

    private fun setUpObserver() {

        mViewModel.forgotPasswordResponse.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it.status) {
                    showMessage(it.message, true)
                    replaceFragment(
                        fragment = VerifyOTPFragment.newInstance(
                            inputData = mViewModel.mEmailIdOrPassword,
                            otp = it.message
                        ),
                        addToBackStack = true
                    )
                } else {
                    try {
                        dialog.setViews(
                            it.message
                        )
                        dialog.show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })

    }

    private fun fetchErrorResponse(response: ForgotPasswordResponse): StringBuilder {

        val stringBuilder = StringBuilder()
        val jsonStrinData = Gson().toJson(response)
        val convertedObject: JsonObject =
            Gson().fromJson(jsonStrinData, JsonObject::class.java)

        if (convertedObject.has("email_mobile")) {
            stringBuilder.append(convertedObject.getAsJsonArray("email_mobile").get(0))
            stringBuilder.append("\n");
        }
        return stringBuilder;

    }


    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {
            //Next button
            btnSendOtp.setOnClickListener {

                if (validate()) {
                    val forgotPasswordRequest: ForgotPasswordRequest =
                        ForgotPasswordRequest(mViewModel.mEmailIdOrPassword)
                    mViewModel.callForgotPasswordAPI(forgotPasswordRequest)

                }
            }
        }
    }


    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        var isValid = true

        mViewModel.run {
            mBinding.run {

                if (mEmailIdOrPassword.trim().isEmpty()) {
                    edtMobileNo.error = resources.getString(R.string.enter_email_or_password)
                    isValid = false
                }
            }
        }
        return isValid
    }


}