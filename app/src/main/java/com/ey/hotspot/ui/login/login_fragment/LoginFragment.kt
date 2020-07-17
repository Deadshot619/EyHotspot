package com.ey.hotspot.ui.login.login_fragment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginBinding
import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.request.SocialLoginRequest
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.registration.register_user.RegisterUserFragment
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.utils.captcha.TextCaptcha
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
import org.json.JSONObject
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

    override fun getLayoutId() = R.layout.fragment_login
    override fun getViewModel() = LoginFragmentViewModel::class.java

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
        setUpCaptcha()
    }

    private fun setUpCaptcha() {


        val textCaptcha =
            TextCaptcha(100, 50, 4, TextCaptcha.TextOptions.LETTERS_ONLY)
        mBinding.ivCaptcha.setImageBitmap(textCaptcha.getImage());

    }

    private fun setUpObservers() {


        mViewModel.loginResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {

                showMessage(it.message, true)
                goToHomePage()
            } else {
                showMessage(it.message, true)
            }
        })


        mViewModel.socialLoginResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                showMessage(it.message, true)
               goToHomePage()
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



        //Register New user
        mBinding.tvGetStarted.setOnClickListener {
            replaceFragment(
                fragment = RegisterUserFragment.newInstance(),
                addToBackStack = true,
                bundle = null
            )
        }

        //Facebook
        mBinding.ivFacebookSignIn.setOnClickListener {
            facebookSign()
        }

        //Google
        mBinding.ivGoogleSignIn.setOnClickListener {
            signIn()
        }

        //Skip button
        mBinding.btnSkip.setOnClickListener {
            mViewModel.setSkippedUserData()
            goToHomePage()
        }
    }

    //Method to redirect user to home page
    private fun goToHomePage(){
        startActivity(Intent(activity, BottomNavHomeActivity::class.java))
        activity?.finish()
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

                        if (checkFacebookResponse(`object`)) {

                            val socialLoginRequest: SocialLoginRequest = SocialLoginRequest(
                                `object`.getString("first_name"), `object`.getString("last_name"),
                                `object`.getString("email"), resources.getString(R.string.facebook),
                                `object`.getString("id"), accessToken
                            )

                            mViewModel.callSocialLogin(socialLoginRequest)
                        } else {
                            showMessage(resources.getString(R.string.insufficient_data), true)
                        }
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,first_name,last_name,email")
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

    private fun checkFacebookResponse(jsonObject: JSONObject): Boolean {

        var status: Boolean = false;
        var id = "";
        var firstName = "";
        var lastName = "";
        var email = ""
        if (jsonObject.has("id")) {
            id = jsonObject.getString("id")
        }
        if (jsonObject.has("first_name")) {
            firstName = jsonObject.getString("first_name")
        }
        if (jsonObject.has("last_name")) {
            lastName = jsonObject.getString("last_name")
        }
        if (jsonObject.has("email")) {
            email = jsonObject.getString("email")
        }


        if ((id.isEmpty()) || (id.isBlank())) {
            status = false
        } else if ((firstName.isEmpty()) || (firstName.isBlank())) {
            status = false

        } else if ((lastName.isEmpty()) || (lastName.isBlank())) {
            status = false

        } else {
            status = !((email.isEmpty()) || (email.isBlank()))
        }

        return status


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
            val googleFirstName = account?.givenName ?: ""
            val googleLastName = account?.familyName ?: ""
            val googleEmail = account?.email ?: ""
            val googleIdToken = account?.idToken ?: ""


            val socialLoginRequest: SocialLoginRequest = SocialLoginRequest(
                googleFirstName,
                googleLastName,
                googleEmail,
                resources.getString(R.string.google_provider),
                googleId,
                googleIdToken
            )

            mViewModel.callSocialLogin(socialLoginRequest)


        } catch (e: ApiException) {

            showMessage(resources.getString(R.string.google_sign_failed), true)
        }
    }


}