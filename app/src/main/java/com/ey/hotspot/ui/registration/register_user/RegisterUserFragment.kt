package com.ey.hotspot.ui.registration.register_user

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRegisterUserBinding
import com.ey.hotspot.network.request.RegisterRequest
import com.ey.hotspot.ui.login.permission.PermissionFragment
import com.ey.hotspot.ui.registration.register_user.model.RegistrationResponse
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.utils.constants.convertStringFromList
import com.ey.hotspot.utils.dialogs.OkDialog
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage
import com.ey.hotspot.utils.validations.isEmailValid
import com.ey.hotspot.utils.validations.isValidMobile
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_register_user.*
import java.util.*


class RegisterUserFragment : BaseFragment<FragmentRegisterUserBinding, RegisterUserViewModel>() {


    //Create 'OK' Dialog
    val dialog by lazy {
        OkDialog(requireContext()).apply {
            setViews(
                title = "",
                okBtn = {
                    this.dismiss()
                }
            )
        }
    }

    //Facebook Sign In
    lateinit var callbackManager: CallbackManager
    var name = ""
    var accessToken = ""

    //Google Sign In
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001


    companion object {
        fun newInstance() = RegisterUserFragment()
    }

    override fun getLayoutId() = R.layout.fragment_register_user
    override fun getViewModel() = RegisterUserViewModel::class.java

    override fun onBinding() {
        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpUIData()

        setUpListeners()

        setUpObservers()
    }

    private fun setUpObservers() {


        mViewModel.registrationResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            it.getContentIfNotHandled()?.let {

                if (it.status) {

                    showMessage(it.message, true)

                    replaceFragment(
                        fragment = RegistrationOptionFragment.newInstance(
                            emailID = mViewModel.emailId,
                            phoneNo = mViewModel.mobileNumber
                        ),
                        addToBackStack = true

                    )
                } else {

                    try {
                        dialog.setViews(
                            convertStringFromList(
                                it.data.firstName,
                                it.data.lastName,
                                it.data.email,
                                it.data.countryCode,
                                it.data.mobileNo,
                                it.data.password,
                                it.data.confirmPassword
                            )
                        )
                        dialog.show()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            })


    }


    private fun setUpUIData() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.register_with_us),
            showUpButton = true
        )
        tvTermsCondition.paintFlags = tvTermsCondition.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        setUpFacebookLogin()
        setuPGoogelSignIn()
    }

    private fun setuPGoogelSignIn() {

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.google_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

    }

    private fun setUpFacebookLogin() {


        callbackManager = CallbackManager.Factory.create()
        FacebookSdk.setIsDebugEnabled(true)
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)

        if (isLoggedIn()) {
            // Show the Activity with the logged in user
        } else {
            // Show the Home Activity
        }
    }

    /**
     * Method to setup click listeners
     */
    private fun setUpListeners() {
        mBinding.run {

            //Sign In button
            btnGetStarted.setOnClickListener {
                if (validate()) {
                    val register: RegisterRequest =
                        RegisterRequest(
                            mViewModel.firstName,
                            mViewModel.lastName,
                            "91",
                            mViewModel.mobileNumber,
                            mViewModel.emailId,
                            mViewModel.password,
                            mViewModel.confirmPassword
                        )


                    mViewModel.registerUser(register)


                }
            }

            tvTermsCondition.setOnClickListener {
                replaceFragment(
                    fragment = PermissionFragment.newInstance(),
                    addToBackStack = true,
                    bundle = null
                )
            }


            //FacebookLogin
            ivFacebookSignIn.setOnClickListener {
                facebookSign()
            }

            //Google Login In
            ivGoogleSignIn.setOnClickListener {
                signIn()
            }


        }
    }

    private fun facebookSign() {

        LoginManager.getInstance().logInWithReadPermissions(
            this,
            Arrays.asList("public_profile", "email")
        );
        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    accessToken = loginResult?.accessToken.toString()

                    val request = GraphRequest.newMeRequest(
                        loginResult!!.accessToken
                    ) { `object`, response ->
                        Log.v("LoginActivity", response.toString())

                        // Application code
                        val email = `object`.getString("email")
                        Toast.makeText(requireActivity(), "" + email, Toast.LENGTH_SHORT).show()

                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,email")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    Toast.makeText(requireActivity(), "Login Cancelled", Toast.LENGTH_LONG)
                        .show()

                }

                override fun onError(exception: FacebookException) {
                    Toast.makeText(requireActivity(), exception.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
    }


    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(requireActivity()) {
                // Update your UI here
            }
    }

    private fun revokeAccess() {
        mGoogleSignInClient.revokeAccess()
            .addOnCompleteListener(requireActivity()) {
                // Update your UI here
            }
    }


    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        var firstName: Boolean = false
        var lastName: Boolean = false
        var mobileNo: Boolean = true
        var emailId: Boolean = false
        var password: Boolean = false
        var confirmPassword: Boolean = false
        var checkPasswordConfirmPassword = false


        if (mViewModel.firstName.trim().isEmpty()) {
            mBinding.edtFirstName.error = resources.getString(R.string.invalid_firstName)
            firstName = false
        } else {
            firstName = true
        }

        if (mViewModel.lastName.trim().isEmpty()) {
            mBinding.edtLastName.error = resources.getString(R.string.invalid_last_name_label)
            lastName = false
        } else {
            lastName = true
        }

        if (mViewModel.mobileNumber.trim().isNotEmpty())
            if (!mViewModel.mobileNumber.isValidMobile()) {
                edtMobileNo.error = resources.getString(R.string.invalid_mobile)
                mobileNo = false
            } else {
                mobileNo = true
            }

        if (!mViewModel.emailId.isEmailValid()) {
            mBinding.edtEmail.error = resources.getString(R.string.invalid_email_label)
            emailId = false
        } else {
            emailId = true
        }

        if (mViewModel.password.trim().isEmpty()) {
            mBinding.edtPassword.error = resources.getString(R.string.invalid_password)
            password = false
        } else {

            password = true
        }
        if (mViewModel.confirmPassword.trim().isEmpty()) {
            mBinding.edtConfirmPassword.error =
                resources.getString(R.string.invalid_confirm_password)
            confirmPassword = false
        } else {
            confirmPassword = true
        }

        if (mViewModel.password.equals(mViewModel.confirmPassword)) {
            if ((mViewModel.password.isEmpty() && mViewModel.confirmPassword.isEmpty())) {
                mBinding.edtPassword.error = resources.getString(R.string.pwd_confirm_pwd_empty)
                mBinding.edtConfirmPassword.error =
                    resources.getString(R.string.pwd_confirm_pwd_empty)
                checkPasswordConfirmPassword = false
            } else {
                checkPasswordConfirmPassword = true
                mBinding.edtPassword.error = null
                mBinding.edtConfirmPassword.error = null
            }
        } else {
            mBinding.edtPassword.error = resources.getString(R.string.pwd_not_match)
            mBinding.edtConfirmPassword.error = resources.getString(R.string.pwd_not_match)
            checkPasswordConfirmPassword = false
        }

        if (firstName == true && lastName == true &&
            mobileNo == true && emailId == true &&
            password == true && confirmPassword == true &&
            checkPasswordConfirmPassword == true
        ) {
            return true
        } else {
            return false
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        return isLoggedIn
    }


    fun logOutUser() {
        LoginManager.getInstance().logOut()
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""
            Log.i("Google ID", googleId)
            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)
            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)
            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)
            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)
            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }
}