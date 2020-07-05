package com.ey.hotspot.ui.settings.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.SettingsFragmentBinding

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


    }


}