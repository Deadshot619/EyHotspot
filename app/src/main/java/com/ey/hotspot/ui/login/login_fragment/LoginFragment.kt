package com.ey.hotspot.ui.login.login_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginBinding
import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.registration.register_user.RegisterUserFragment
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.utils.constants.OptionType
import com.ey.hotspot.utils.replaceFragment
import com.ey.hotspot.utils.showMessage
import com.ey.hotspot.utils.validations.isEmailValid
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.util.*


class LoginFragment : BaseFragment<FragmentLoginBinding, LoginFragmentViewModel>() {


    //Facebook Sign In
    lateinit var callbackManager: CallbackManager
    var name = ""
    var accessToken = ""

    //Google Sign In
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001


    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun getViewModel(): Class<LoginFragmentViewModel> {
        return LoginFragmentViewModel::class.java
    }

    override fun onBinding() {


        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpUIData()
        setUpListeners()
        setUpObservers()
    }

    private fun setUpUIData() {

        setUpFacebookLogin()
        setuPGoogelSignIn()
    }

    private fun setUpObservers() {


        mViewModel.loginResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {

                showMessage(it.message, true)
                val homeIntent = Intent(activity, BottomNavHomeActivity::class.java)
                startActivity(homeIntent)
                activity?.finish()
            } else {
                showMessage(it.message, true)
            }
        })
    }

    private fun setUpListeners() {
        //Submit
        mBinding.btnSignIn.setOnClickListener {

            if (validate()) {

                val loginRequest: LoginRequest =
                    LoginRequest(
                        mViewModel.emailId,
                        mViewModel.password
                    )
                mViewModel.callLogin(loginRequest)

            }

        }

        //Forgot Password
        mBinding.tvForgotPassword.setOnClickListener {
            replaceFragment(
                fragment = RegistrationOptionFragment.newInstance(),
                addToBackStack = true,
                bundle = Bundle().apply {
                    putString(
                        RegistrationOptionFragment.TYPE_KEY,
                        OptionType.TYPE_FORGOT_PASSWORD.name
                    )
                })
        }

        //Register New user
        mBinding.tvGetStarted.setOnClickListener {

            replaceFragment(
                fragment = RegisterUserFragment.newInstance(),
                addToBackStack = true,
                bundle = null
            )
        }


        mBinding.ivFacebookSignIn.setOnClickListener {

            facebookSign()
        }

        mBinding.ivGoogleSignIn.setOnClickListener {

            signIn()

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

    private fun setuPGoogelSignIn() {

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.google_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

    }

    /**
     * Method to validate input fields
     */
    private fun validate(): Boolean {
        mViewModel.run {
            mBinding.run {
                return if (!emailId.isEmailValid()) {
                    etEmailId.error = resources.getString(R.string.invalid_email)
                    false
                } else if (password.trim().isEmpty()) {
                    etPassword.error = resources.getString(R.string.enter_password)
                    false
                } else
                    true
            }
        }
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