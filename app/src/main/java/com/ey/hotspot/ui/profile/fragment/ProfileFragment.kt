package com.ey.hotspot.ui.profile.fragment

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ProfileFragmentBinding

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>() {


    override fun getLayoutId() = R.layout.profile_fragment

    override fun getViewModel() = ProfileViewModel::class.java

    override fun onBinding() {

        setToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.my_profile_label), showUpButton = true)

    }
}