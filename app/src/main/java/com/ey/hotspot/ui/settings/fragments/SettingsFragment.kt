package com.ey.hotspot.ui.settings.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.recreate
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.SettingsFragmentBinding
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListFragment
import com.ey.hotspot.utils.MyHotSpotSharedPreference
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.replaceFragment
import com.ey.stringlocalization.utils.LanguageManager
import kotlinx.android.synthetic.main.custom_confirm_settings_dialog.view.*

class SettingsFragment : BaseFragment<SettingsFragmentBinding, SettingsViewModel>() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun getLayoutId() = R.layout.settings_fragment
    override fun getViewModel() = SettingsViewModel::class.java

    var toggleChangeLanguage: Boolean = false

    override fun onBinding() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.setting_label),
            showUpButton = true
        )

        setUpListeners()
    }

    private fun setUpListeners() {

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        //Wifi Log List
        mBinding.llWifiLogList.setOnClickListener {
            replaceFragment(WifiLogListFragment(), true)
        }

        /*Submit click*/
        mBinding.btnSubmit.setOnClickListener {
            showConfirmDialog("", "")
        }

        mBinding.ivChangeLanguage.setOnClickListener {

            if (toggleChangeLanguage) {


                mBinding.ivChangeLanguage.resources.getDrawable(R.drawable.ic_active_switch)
                val langType = HotSpotApp.prefs!!.getLanguage()
                if (langType == Constants.ENGLISH_LANG) {
                    HotSpotApp.prefs!!.setLanguage(Constants.ARABIC_LANG)
                    restartApplication(requireActivity(), HotSpotApp.prefs!!)
                }

                toggleChangeLanguage = false
            } else {
                mBinding.ivChangeLanguage.resources.getDrawable(R.drawable.ic_inactive_switch)

                val langType = HotSpotApp.prefs!!.getLanguage()
                if (langType == Constants.ENGLISH_LANG) {
                    HotSpotApp.prefs!!.setLanguage(Constants.ARABIC_LANG)
                    restartApplication(requireActivity(), HotSpotApp.prefs!!)
                }

                toggleChangeLanguage = true
            }
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

    fun restartApplication(context: Context?, thobeSharedPreference: MyHotSpotSharedPreference) {
        LanguageManager.setLanguage(requireContext()!!, thobeSharedPreference.getLanguage())
        /**
         * restart the application for API > 25 otherwise just recreate
         */
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            restartApp()
        } else {
            /**
             * pop back fragment before activity recreating
             */

            activity?.recreate()
        }
    }

    fun restartApp() {
        val i = requireActivity().baseContext.packageManager
            .getLaunchIntentForPackage(requireActivity().baseContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        requireActivity().finish()
        startActivity(i)
    }


}