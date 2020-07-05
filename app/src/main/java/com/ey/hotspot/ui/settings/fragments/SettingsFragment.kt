package com.ey.hotspot.ui.settings.fragments

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
import com.ey.hotspot.databinding.SettingsFragmentBinding
import kotlinx.android.synthetic.main.content_resend_link_dialog.view.*
import kotlinx.android.synthetic.main.custom_confirm_settings_dialog.view.*

class SettingsFragment : BaseFragment<SettingsFragmentBinding, SettingsViewModel>() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun getLayoutId(): Int {

        return R.layout.settings_fragment
    }

    override fun getViewModel(): Class<SettingsViewModel> {

        return SettingsViewModel::class.java
    }

    override fun onBinding() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.setting_label),
            showUpButton = true
        )

        mBinding.btnSubmit.setOnClickListener {

            showConfirmDialog("", "")
        }

    }


    fun showConfirmDialog(str_msg: String, str_action: String) {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val inflater = this.getLayoutInflater()

        @SuppressLint("InflateParams")
        val dialogView = inflater.inflate(R.layout.custom_confirm_settings_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(true)

        val btYes = dialogView.btYes
        val btNo = dialogView.btNo

        btNo.setOnClickListener {
            alertDialog.dismiss()
        }

        btYes.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }


}