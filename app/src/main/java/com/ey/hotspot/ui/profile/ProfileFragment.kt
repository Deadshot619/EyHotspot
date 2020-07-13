package com.ey.hotspot.ui.profile

import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.ProfileFragmentBinding
import com.ey.hotspot.ui.profile.fragment.model.UpdateProfileRequest
import com.ey.hotspot.ui.settings.fragments.SettingsFragment
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage
import com.ey.hotspot.utils.validations.isEmailValid
import com.ey.hotspot.utils.validations.isValidMobile

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
        setUpListener()
        setUpObserver()


    }

    private fun setUpListener() {
        //Settings button
        mBinding.toolbarLayout.ivSettings.setOnClickListener {
            replaceFragment(
                fragment = SettingsFragment.newInstance(),
                addToBackStack = true,
                bundle = null
            )
        }

//        Update Profile
        mBinding.btnUpdateProfile.setOnClickListener {

            if (validate()) {
                mViewModel.updateProfile(
                    UpdateProfileRequest(
                        firstName = mViewModel.profileData.value!!.firstName,
                        lastName = mViewModel.profileData.value!!.lastName,
                        mobileNo = mViewModel.profileData.value!!.mobileNo,
                        countryCode = "91",
                        email = mViewModel.profileData.value!!.emailId
                    )
                )
            }



        }


    }

    private fun setUpObserver() {
        mViewModel.profileResponse.observe(viewLifecycleOwner, Observer {
                it.getContentIfNotHandled()?.let {content ->
                    //showMessage(content.message, false)
                }
        })
    }

    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        mViewModel.profileData.value?.run {
            mBinding.run {
                return if (firstName.trim().isEmpty()) {
                    edtFirstName.error = resources.getString(R.string.invalid_firstName)
                    false
                } else if (lastName.trim().isEmpty()) {
                    edtLastName.error = resources.getString(R.string.invalid_last_name_label)
                    false
                } else if (!emailId.isEmailValid()) {
                    edtEmail.error = resources.getString(R.string.invalid_email_label)
                    false
                } else if (!mobileNo.isValidMobile()) {
                    edtMobileNo.error = resources.getString(R.string.invalid_mobile)
                    false
                } else true
            }
        } ?: return false
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}