package com.ey.hotspot.ui.login.login_fragment.permission

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentPermissionBinding


class PermissionFragment : BaseFragment<FragmentPermissionBinding, PermissionViewModel>() {


    override fun getLayoutId(): Int {
        return R.layout.fragment_permission
    }

    override fun getViewModel(): Class<PermissionViewModel> {

        return PermissionViewModel::class.java
    }

    override fun onBinding() {

    }



}