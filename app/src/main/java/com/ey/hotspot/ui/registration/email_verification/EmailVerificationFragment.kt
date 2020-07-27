package com.ey.hotspot.ui.registration.email_verification

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentEmailVerificationBinding
import com.ey.hotspot.ui.login.LoginActivity
import kotlinx.android.synthetic.main.content_resend_link_dialog.view.*

class EmailVerificationFragment :
    BaseFragment<FragmentEmailVerificationBinding, EmailVerificationViewModel>() {

    companion object {
        fun newInstance() = EmailVerificationFragment()
    }

    override fun getLayoutId() = R.layout.fragment_email_verification
    override fun getViewModel() = EmailVerificationViewModel::class.java
    override fun onBinding() {
        setUpToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.registration_label), showUpButton = true)
        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpListeners()
    }

    private fun setUpListeners() {


        mBinding.tvResendLinkLabel.setOnClickListener {

            showConfirmDialog(resources.getString(R.string.verify_link_msg) +"prashant.jadhav@gmail.com", "")

        }

        mBinding.btnSignIn.setOnClickListener {

            //replaceFragment(fragment = LoginFragment.newInstance(),addToBackStack = false,bundle = null)

            val homeIntent = Intent(activity, LoginActivity::class.java)
            startActivity(homeIntent)

        }
    }


    fun showConfirmDialog(str_msg: String, str_action: String) {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = this.getLayoutInflater()

        @SuppressLint("InflateParams")
        val dialogView = inflater.inflate(R.layout.content_resend_link_dialog, null)
        dialogBuilder.setView(dialogView)


        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(true)
        val txt_dialog_title = dialogView.tvEmailId
        val buttonOk = dialogView.btn_sign_in


        txt_dialog_title.setText( str_msg)

        buttonOk.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


}