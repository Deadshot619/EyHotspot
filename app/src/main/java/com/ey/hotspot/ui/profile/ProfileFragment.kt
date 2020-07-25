package com.ey.hotspot.ui.profile

import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ProfileFragmentBinding
import com.ey.hotspot.ui.profile.fragment.model.UpdateProfileRequest
import com.ey.hotspot.ui.settings.fragments.SettingsFragment
import com.ey.hotspot.utils.constants.convertStringFromList
import com.ey.hotspot.utils.dialogs.OkDialog
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage
import com.ey.hotspot.utils.validations.isEmailValid
import com.ey.hotspot.utils.validations.isValidMobile
import com.ey.hotspot.utils.validations.isValidName
import com.ey.hotspot.utils.validations.isValidPassword

class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>() {

    val dialog by lazy {
        OkDialog(requireContext()).apply {
            setViews { this.dismiss() }
        }
    }

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
            showUpButton = true,
            showSettingButton = false
        )
        setUpDataView()
        setUpListener()
        setUpObserver()


    }

    private fun setUpDataView() {
        if (!HotSpotApp.prefs?.getAppLoggedInStatus()!!)
            mBinding.btnUpdateProfile.visibility = View.GONE


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
                        mobileNo = mViewModel.profileData.value?.mobileNo,
                        countryCode = "91",
                        email = mViewModel.profileData.value!!.emailId
                    )
                )
            }

        }


    }

    private fun setUpObserver() {
        mViewModel.profileResponse.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { content ->
                //showMessage(content.message, false)
            }
        })


        //Profile Error
        mViewModel.profileError.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { response ->
                dialog.setViews(
                    convertStringFromList(
                        response.firstName,
                        response.lastName,
                        response.email,
                        response.countryCode,
                        response.mobileNo,
                        response.password,
                        response.confirmPassword
                    )
                )
                dialog.show()
            }
        })

        mViewModel.getCountryCodeList.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {

                if (it.status == true) {
                  /*  for (i in 0 until it.data.country_codes.size) {
                        val countryName: String = it.data.country_codes.get(i).value
                    }*/

                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        it.data.country_codes.map { it.value }.toList()
                    )

//                    mBinding.spinnerIssue.adapter = adapter
                } else {
                    showMessage(it.message, true)
                }

              
            }

        })
    }

    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        var isValid = true

        mViewModel.profileData.value?.run {
            mBinding.run {
                if (!firstName.isValidName()) {
                    edtFirstName.error = resources.getString(R.string.invalid_firstName)
                    isValid = false
                }
                if (!lastName.isValidName()) {
                    edtLastName.error = resources.getString(R.string.invalid_last_name_label)
                    isValid = false
                }
                if (!emailId.isEmailValid()) {
                    edtEmail.error = resources.getString(R.string.invalid_email_label)
                    isValid = false
                }
                if (!mobileNo.isValidMobile()) {
                    edtMobileNo.error = resources.getString(R.string.invalid_mobile)
                    isValid = false
                }
                if (password.isNotEmpty() || confirmPassword.isNotEmpty()) {     //Check only if password are written
                    if (!password.isValidPassword()){
                        edtPassword.error = resources.getString(R.string.password_format)
                        isValid = false
                    }  else if(password != confirmPassword) {
                        edtPassword.error = resources.getString(R.string.pwd_not_match)
                        edtConfirmPassword.error = resources.getString(R.string.pwd_not_match)
                        isValid = false
                    }
                }
            }
        } ?: return false

        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()

    }
}