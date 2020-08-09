package com.ey.hotspot.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.ProfileFragmentBinding
import com.ey.hotspot.network.request.UpdateProfileRequest
import com.ey.hotspot.ui.settings.fragments.SettingsFragment
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.convertStringFromList
import com.ey.hotspot.utils.dialogs.OkDialog
import com.ey.hotspot.utils.extention_functions.logoutUser
import com.ey.hotspot.utils.extention_functions.replaceFragment
import com.ey.hotspot.utils.extention_functions.showMessage
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
                        countryCode = mViewModel.profileData.value?.countryCode.toString(),
                        email = mViewModel.profileData.value!!.emailId,
                        password = mViewModel.profileData.value?.password,
                        confirmPassword = mViewModel.profileData.value?.confirmPassword
                    )
                )
            }
        }

        //Country Code
        mBinding.spinnerIssue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //set country code in profile
                mViewModel.profileData.value?.countryCode =
                    mViewModel.getCountryCodeList.value?.peekContent()?.data?.country_codes?.find {
                        it.value == mBinding.spinnerIssue.selectedItem.toString()
                    }?.key ?: -1
            }
        }


    }

    private fun setUpObserver() {
        //Profile Response
        mViewModel.profileResponse.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { content ->
                //showMessage(content.message, false)
                mBinding.spinnerIssue.setSelection(
                    mViewModel.getCountryCodeList.value?.peekContent()?.data?.country_codes?.indexOfFirst { it.key == content.data.country_code }
                        ?: -1
                )

                disableViewsWhenSocialLogin(content.data.provider)
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

        //Country Code
        mViewModel.getCountryCodeList.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                if (it.status) {
                    val adapter = ArrayAdapter<String>(
                        requireContext(),
                        R.layout.item_country_code,
                        it.data.country_codes.map { it.value }.toList()
                    )

                    mBinding.spinnerIssue.adapter = adapter
                } else {
                    showMessage(it.message, true)
                }
            }
        })


        //Email is changed
        mViewModel.emailChange.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
//                callVerificationFragment(it.tmp_token, mViewModel.profileData.value?.emailId!!)
                activity?.application?.logoutUser(Bundle().apply {
                    putBoolean(Constants.GO_TO_VERIFICATION_FRAGMENT, true)
                    putString(Constants.EMAIL_ID, mViewModel.profileData.value?.emailId)
                    putString(Constants.TEMP_TOKEN, it.tmp_token)
                })
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


                if (firstName.trim().isEmpty()) {
                    edtFirstName.error = resources.getString(R.string.empty_firstName)
                    isValid = false

                } else if (!firstName.trim().isValidName()) {
                    edtFirstName.error = resources.getString(R.string.invalid_Name)
                    isValid = false
                }


                /*if(lastName.isEmpty()){
                    edtLastName.error = resources.getString(R.string.empty_lastName)
                    isValid = false
                }else if(!lastName.isValidName()){
                    edtLastName.error = resources.getString(R.string.invalid_Name)
                    isValid = false
                }*/

                if (lastName.trim().isNotEmpty() && !lastName.trim().isValidName()) {
                    edtLastName.error = resources.getString(R.string.invalid_Name)
                    isValid = false
                }


                /*    if (!firstName.isValidName()) {
                        edtFirstName.error = resources.getString(R.string.invalid_firstName)
                        isValid = false
                    }
                    if (!lastName.isValidName()) {
                        edtLastName.error = resources.getString(R.string.invalid_last_name_label)
                        isValid = false
                    }
                    */


                if (!emailId.isEmailValid()) {
                    edtEmail.error = resources.getString(R.string.invalid_email_label)
                    isValid = false
                }
                if (mobileNo.trim().isNotEmpty() && !mobileNo.isValidMobile()) {
                    edtMobileNo.error = resources.getString(R.string.invalid_mobile)
                    isValid = false
                }
                if (password.isNotEmpty() || confirmPassword.isNotEmpty()) {     //Check only if password are written
                    if (!password.isValidPassword()) {
                        edtPassword.error = resources.getString(R.string.password_format)
                        isValid = false
                    } else if (password != confirmPassword) {
                        edtPassword.error = resources.getString(R.string.pwd_not_match)
                        edtConfirmPassword.error = resources.getString(R.string.pwd_not_match)
                        isValid = false
                    }
                }
            }
        } ?: return false

        return isValid
    }

    /**
     * This method will disable/hide certain views when user has logged in via social media
     */
    private fun disableViewsWhenSocialLogin(provider: String?) {
        //  login from facebook/google disable emailid
//        if (mViewModel.profileData.value?.provider.equals("google") || mViewModel.profileData.value?.provider.equals("facebook")) {
        if (!provider.isNullOrEmpty()) {
            mBinding.run {
                //email
                edtEmail.isFocusable = false
                edtEmail.isEnabled = false

                //Password
                passwordInputLayout.visibility = View.GONE
                tvPasswordHint.visibility = View.GONE

                //Confirm Password
                confirmPasswordInputLayout.visibility = View.GONE
                tvConfirmPasswordHint.visibility = View.GONE

                //Password Info
                tvPasswordMsg.visibility = View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

    }
}