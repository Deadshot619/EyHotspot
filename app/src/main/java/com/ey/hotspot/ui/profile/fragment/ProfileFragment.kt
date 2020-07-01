package com.ey.hotspot.ui.profile.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ProfileFragmentBinding

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>() {


    override fun getLayoutId(): Int {
        return R.layout.profile_fragment
    }

    override fun getViewModel(): Class<ProfileViewModel> {
        return ProfileViewModel::class.java
    }

    override fun onBinding() {


    }


}