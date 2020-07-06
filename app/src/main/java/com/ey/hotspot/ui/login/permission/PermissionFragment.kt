package com.ey.hotspot.ui.login.permission

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentPermissionBinding
import com.ey.hotspot.ui.registration.email_verification.CompleteRegistrationFragment
import com.ey.hotspot.utils.removeFragment


class PermissionFragment : BaseFragment<FragmentPermissionBinding, PermissionViewModel>() {


    companion object {
        fun newInstance() = PermissionFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_permission
    }

    override fun getViewModel(): Class<PermissionViewModel> {

        return PermissionViewModel::class.java
    }

    override fun onBinding() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.permission_label),
            showUpButton = true
        )



        mBinding.btnSubmit.setOnClickListener {

            removeFragment(this)
        }
    }


}