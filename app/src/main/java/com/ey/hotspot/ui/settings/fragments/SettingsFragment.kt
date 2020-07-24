package com.ey.hotspot.ui.settings.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.FragmentSettingsBinding
import com.ey.hotspot.ui.profile.ProfileFragment
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListFragment
import com.ey.hotspot.utils.LanguageManager
import com.ey.hotspot.utils.MyHotSpotSharedPreference
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.goToLoginScreen
import com.ey.hotspot.utils.dialogs.YesNoDialog
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage
import kotlinx.android.synthetic.main.custom_confirm_settings_dialog.view.*

class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>() {

    //Create 'OK' Dialog
    val dialog by lazy {
        YesNoDialog(requireContext()).apply {
            setViews(
                title = getString(R.string.logout_confirm),
                description = "",
                yes = { goToLoginScreen() },
                no = { this.dismiss() }
            )
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun getLayoutId() = R.layout.fragment_settings
    override fun getViewModel() = SettingsViewModel::class.java

    var toggleChangeLanguage: Boolean = false

    override fun onBinding() {
        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }


        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.setting_label),
            showUpButton = false
        )

        setUpListeners()

        hideViewsIfSkippedUser()

        //Change image of language
        if(HotSpotApp.prefs?.getLanguage() == Constants.ARABIC_LANG)
            mBinding.ivChangeLanguage.setImageResource(R.drawable.ic_active_switch)
        else
            mBinding.ivChangeLanguage.setImageResource(R.drawable.ic_inactive_switch)
    }

    private fun setUpListeners() {

        //Wifi Log List
        mBinding.llWifiLogList.setOnClickListener {
            showMessage(resources.getString(R.string.under_construction_label))
            //replaceFragment(WifiLogListFragment(), true)
        }

        /*Submit click*/
/*
        mBinding.btnSubmit.setOnClickListener {
            showConfirmDialog("", "")
        }
*/

        //Change Language
        mBinding.ivChangeLanguage.setOnClickListener {
            val langType = HotSpotApp.prefs!!.getLanguage()
            if (langType == Constants.ENGLISH_LANG) {
                HotSpotApp.prefs!!.setLanguage(Constants.ARABIC_LANG)
                restartApplication(requireActivity(), HotSpotApp.prefs!!)
            } else if (langType == Constants.ARABIC_LANG) {

                HotSpotApp.prefs!!.setLanguage(Constants.ENGLISH_LANG)
                restartApplication(requireActivity(), HotSpotApp.prefs!!)
            }
        }

        //Profile
        mBinding.llProfileList.setOnClickListener {
            replaceFragment(fragment = ProfileFragment(), addToBackStack = true)
        }

        //Logout
        mBinding.llLogout.setOnClickListener {
            dialog.show()
        }
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


    private fun hideViewsIfSkippedUser(){
        if(HotSpotApp.prefs!!.getSkipStatus())
            mBinding.run {
                llProfileList.visibility = View.GONE
                llLogout.visibility = View.GONE
            }
    }
}