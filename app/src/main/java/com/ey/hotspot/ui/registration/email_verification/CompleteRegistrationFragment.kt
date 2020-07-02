package com.ey.hotspot.ui.registration.email_verification

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.CompleteRegistrationFragmentBinding
import kotlinx.android.synthetic.main.content_resend_link_dialog.view.*

class CompleteRegistrationFragment : BaseFragment<CompleteRegistrationFragmentBinding, CompleteRegistrationViewModel>() {

    companion object {
        fun newInstance() = CompleteRegistrationFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.complete_registration_fragment
    }

    override fun getViewModel(): Class<CompleteRegistrationViewModel> {

        return CompleteRegistrationViewModel::class.java
    }

    override fun onBinding() {

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }


       mBinding.btnSignIn.setOnClickListener {
           showConfirmDialog(resources.getString(R.string.verify_link_msg) +"prashant.jadhav@gmail.com", "")
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