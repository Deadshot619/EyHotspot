package com.ey.hotspot.ui.profile.fragment

import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ProfileFragmentBinding
import com.ey.hotspot.ui.profile.updateprofile.model.UpdateProfileRequest
import com.ey.hotspot.ui.settings.fragments.SettingsFragment
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>() {


    override fun getLayoutId() = R.layout.profile_fragment

    override fun getViewModel() = ProfileViewModel::class.java

    override fun onBinding() {

        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.my_profile_label),
            showUpButton = false,
            showSettingButton = true
        )
        getUserListData()
        setUpListener()
        setUpObserver()


    }

    private fun getUserListData() {

        mViewModel.getProfileDetails()
    }

    private fun setUpListener() {

        mBinding.toolbarLayout.ivSettings.setOnClickListener {

            replaceFragment(
                fragment = SettingsFragment.newInstance(),
                addToBackStack = true,
                bundle = null
            )
        }

        mBinding.btnUpdateProfile.setOnClickListener {

            val updateProfileRequest: UpdateProfileRequest = UpdateProfileRequest(
                mViewModel.firstName, mViewModel.lastName,
                mViewModel.mobileNo,
                "91", mViewModel.emailId

            )
            mViewModel.updateProfile(updateProfileRequest)
        }


    }

    private fun setUpObserver() {

        mViewModel.profileResponse.observe(viewLifecycleOwner, Observer {
            //showMessage(it.success.firstname, true)

        })


        mViewModel.updateProfileResponse.observe(viewLifecycleOwner, Observer {

            if (it.status == true) {

                showMessage(it.message, true)
            } else {
                showMessage(it.message, true)
            }
        })
        mViewModel.errorText.observe(viewLifecycleOwner, Observer {

            showMessage(it, true)
        })
    }


}