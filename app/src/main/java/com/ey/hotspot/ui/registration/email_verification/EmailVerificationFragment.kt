package com.ey.hotspot.ui.registration.email_verification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentEmailVerificationBinding
import kotlinx.android.synthetic.main.content_resend_link_dialog.view.*

class EmailVerificationFragment :
    BaseFragment<FragmentEmailVerificationBinding, EmailVerificationViewModel>() {

    companion object {
        fun newInstance() = EmailVerificationFragment()
    }

    override fun getLayoutId() = R.layout.fragment_email_verification
    override fun getViewModel() = EmailVerificationViewModel::class.java
    override fun onBinding() {


        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpListeners()
    }

    private fun setUpListeners() {


        mBinding.tvResendLinkLabel.setOnClickListener {

            showConfirmDialog("prashant.jadhav@gmail.com", "")

        }
    }


    fun showConfirmDialog(str_msg: String, str_action: String) {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = this.getLayoutInflater()

        @SuppressLint("InflateParams")
        val dialogView = inflater.inflate(R.layout.content_resend_link_dialog, null)
        dialogBuilder.setView(dialogView)


        val alertDialog = dialogBuilder.create()
        val txt_dialog_title = dialogView.tvEmailId


        txt_dialog_title.setText(resources.getString(R.string.verify_link_msg) + str_msg)

        alertDialog.show()


    }




}