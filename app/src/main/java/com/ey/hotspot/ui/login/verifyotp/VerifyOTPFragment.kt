package com.ey.hotspot.ui.login.verifyotp

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.VerifyOtpFragmentBinding
import com.ey.hotspot.ui.login.changepassword.ChangePasswordFragment
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordResendOTPRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordVerifyOTPRequest
import com.ey.hotspot.ui.login.verifyotp.model.ForgotPasswordVerifyOTPResponse
import com.ey.hotspot.ui.login.verifyotp.model.ResendForgotPasswordOTP
import com.ey.hotspot.utils.dialogs.OkDialog
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage
import com.google.gson.Gson
import com.google.gson.JsonObject

class VerifyOTPFragment :
    BaseFragment<VerifyOtpFragmentBinding, VerifyOTPViewModel>() {

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
    lateinit var otp: String

    companion object {
        fun newInstance(inputData: String) = VerifyOTPFragment().apply {
            arguments = Bundle().apply {
                putString(mInputData, inputData)
            }
        }

        public const val mInputData = "inputData"


    }

    override fun getLayoutId() = R.layout.verify_otp_fragment
    override fun getViewModel() = VerifyOTPViewModel::class.java

    override fun onBinding() {

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.verify_account),
            showUpButton = true
        )

        setUpDataView()
        setUpListener()
        setUpObserver()


    }

    private fun setUpDataView() {

        mBinding.tvCheckEmailLabel.setText(
            resources.getString(R.string.otp_title) + " " + arguments?.getString(
                mInputData
            ) ?: ""
        )


    }

    private fun setUpListener() {


        mBinding.otpView.setOtpCompletionListener {
            otp = it

        }

        mBinding.btnVerify.setOnClickListener {

            if (!(otp.isEmpty()) && (otp.length == 4)) {
                val verifyOTPRequest: ForgotPasswordVerifyOTPRequest =
                    ForgotPasswordVerifyOTPRequest(
                        otp.toInt(),
                        HotSpotApp.prefs!!.getForgotPasswordField()
                    )
                mViewModel.verifyForgotPasswordOTP(verifyOTPRequest)
            } else {
                showMessage(requireActivity().getString(R.string.enter_valid_otp))
            }


        }

        mBinding.btResendOTP.setOnClickListener {

            val forgotPasswordResendOTPRequest: ForgotPasswordResendOTPRequest =
                ForgotPasswordResendOTPRequest(
                    HotSpotApp.prefs!!.getForgotPasswordField()

                )
            mViewModel.resendForgotPasswordOTP(forgotPasswordResendOTPRequest)

        }

    }

    private fun setUpObserver() {


        mViewModel.forgotPasswordVerifyOTPResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {

                showMessage(it.message, true)

                replaceFragment(
                    fragment = ChangePasswordFragment.newInstance(
                        otp = otp
                    ),
                    addToBackStack = true

                )

            } else {
                try {
                    dialog.setViews(
                        fetchErrorResponseVerifyOTP(it.data).toString()
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

        mViewModel.resendOTPResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {

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

    private fun fetchErrorResponseVerifyOTP(data: ForgotPasswordVerifyOTPResponse): StringBuilder {

        val stringBuilder = StringBuilder()
        val jsonStrinData = Gson().toJson(data)
        val convertedObject: JsonObject =
            Gson().fromJson(jsonStrinData, JsonObject::class.java)

        if (convertedObject.has("otp")) {
            stringBuilder.append(convertedObject.getAsJsonArray("otp").get(0))
            stringBuilder.append("\n");
        }

        if (convertedObject.has("email")) {
            stringBuilder.append(convertedObject.getAsJsonArray("email").get(0))
            stringBuilder.append("\n");
        }
        return stringBuilder;
    }

    private fun fetchErrorResponse(data: ResendForgotPasswordOTP): StringBuilder {


        val stringBuilder = StringBuilder()
        val jsonStrinData = Gson().toJson(data)
        val convertedObject: JsonObject =
            Gson().fromJson(jsonStrinData, JsonObject::class.java)

        if (convertedObject.has("email_mobile")) {
            stringBuilder.append(convertedObject.getAsJsonArray("email_mobile").get(0))
            stringBuilder.append("\n");
        }
        return stringBuilder;

    }


}