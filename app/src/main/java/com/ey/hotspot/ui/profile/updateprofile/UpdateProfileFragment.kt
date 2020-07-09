package com.ey.hotspot.ui.profile.updateprofile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.UpdateProfileFragmentBinding

class UpdateProfileFragment : BaseFragment<UpdateProfileFragmentBinding, UpdateProfileViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.update_profile_fragment

    }

    override fun getViewModel(): Class<UpdateProfileViewModel> {

        return UpdateProfileViewModel::class.java
    }

    override fun onBinding() {

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }



        setUpObserver()


    }

    private fun setUpObserver() {

    }


}