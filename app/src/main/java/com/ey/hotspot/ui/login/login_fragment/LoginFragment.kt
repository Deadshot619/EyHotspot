package com.ey.hotspot.ui.login.login_fragment

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.FragmentLoginBinding
import com.ey.hotspot.network.request.LoginRequest
import com.ey.hotspot.network.request.SocialLoginRequest
import com.ey.hotspot.network.response.VerificationPending
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.login.forgorpassword.ForgotPasswordFragment
import com.ey.hotspot.ui.login.otpverification.fragment.OTPVerificationFragment
import com.ey.hotspot.ui.registration.register_user.RegisterUserFragment
import com.ey.hotspot.ui.registration.registration_option.RegistrationOptionFragment
import com.ey.hotspot.ui.registration.webview.WebViewFragment
import com.ey.hotspot.utils.constants.VerificationType
import com.ey.hotspot.utils.constants.convertStringFromList
import com.ey.hotspot.utils.constants.setSkippedUserData
import com.ey.hotspot.utils.constants.updateSharedPreference
import com.ey.hotspot.utils.dialogs.OkDialog
import com.ey.hotspot.utils.extention_functions.generateCaptchaCode
import com.ey.hotspot.utils.extention_functions.replaceFragment
import com.ey.hotspot.utils.extention_functions.showMessage
import com.ey.hotspot.utils.extention_functions.toVerificationPending
import com.ey.hotspot.utils.validations.isEmailValid
import com.ey.hotspot.utils.validations.isValidPassword
import com.facebook.*
import com.facebook.login.LoginBehavior
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

    val dialog by lazy {
        OkDialog(requireContext()).apply {
            setViews { this.dismiss() }
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
        fun newInstance(
            goToVerificationFragment: Boolean,
            email: String? = null,
            tempToken: String? = null
        ) = LoginFragment().apply {
            arguments = Bundle().apply {
                putBoolean(GO_TO_VERIFICATION_FRAGMENT, goToVerificationFragment)
                putString(EMAIL_ID, email)
                putString(TEMP_TOKEN, tempToken)
            }
        }

        private const val GO_TO_VERIFICATION_FRAGMENT = "go_to_verification_fragment"
        private const val EMAIL_ID = "email_id"
        private const val TEMP_TOKEN = "temp_token"
    }

    override fun getLayoutId() = R.layout.fragment_login
    override fun getViewModel() = LoginFragmentViewModel::class.java

    var mCaptcha: String? = null
    var mEnteredCaptch: String? = null
    var countGoToVerification = 0

    override fun onBinding() {


        mBinding.run {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }

        setUpUIData()
        setUpListeners()
        setUpObservers()

        //If true, go to verification fragment
        if (arguments?.getBoolean(GO_TO_VERIFICATION_FRAGMENT)!!) {
            if (countGoToVerification++ < 1)
                callVerificationFragment(
                    token = arguments?.getString(TEMP_TOKEN)!!,
                    email = arguments?.getString(EMAIL_ID)!!
                )
        }

/*        if (HotSpotApp.prefs!!.getSkipStatus())
            mBinding.ivBack.visibility = View.VISIBLE*/
    }

    private fun setUpUIData() {
        setUpFacebookLogin()
        setuPGoogelSignIn()
        setUpCaptcha()
    }

    private fun setUpCaptcha() {
        mCaptcha = activity?.generateCaptchaCode(5)
        mBinding.layoutCaptcha.etCaptchaText.setText(mCaptcha)
    }

    private fun setUpObservers() {
        //Login Response
        mViewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { response ->

                setUpCaptcha()

/*            if (it.status) {
                showMessage(it.message, false)
                updateSharedPreference(it.data!!)
                goToHomePage()
            } else {*/
                showMessage(response.message, true)

//                if (!(it.data?.verification)!!) {
                response.data?.let { data2 ->
                    if (data2.mobile_no.isNullOrEmpty() && !data2.email_id.isNullOrEmpty()) {
                        callVerificationFragment(token = data2.tmpToken!!, email = data2.email_id)
                    } else {
                        callVerificationOptionSelectionFragment(data2.toVerificationPending())
                    }
                }
//                }
//            }
            }
        })

        //Social Login Response
        mViewModel.socialLoginResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                showMessage(it.message, true)
                if (it.data?.istcaccepted!!)
                {
                    goToHomePage()
                }
                else {
                    replaceFragment(
                        fragment = WebViewFragment.newInstance("login"),
                        addToBackStack = false,
                        bundle = null
                    )

                }
            }
        })

        //Login Error
        mViewModel.loginError.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { response ->
                dialog.setViews(convertStringFromList(response.email_error, response.password))
                dialog.show()
            }
        })
    }


    private fun callVerificationFragment(token: String, email: String) {
        HotSpotApp.prefs!!.setRegistrationTempToken(token)
        HotSpotApp.prefs!!.setRegistrationEmailID(email)

        replaceFragment(
            fragment = OTPVerificationFragment.newInstance(
                selectedOption = VerificationType.EMAIL,
                selectedItem = email
            ), addToBackStack = true
        )
    }

    private fun callVerificationOptionSelectionFragment(response: VerificationPending) {

        HotSpotApp.prefs!!.setRegistrationTempToken(response.tmpToken!!)
        HotSpotApp.prefs!!.setRegistrationEmailID(response.email_id!!)

        replaceFragment(
            fragment = RegistrationOptionFragment.newInstance(
                emailID = response.email_id,
                phoneNo = response.mobile_no!!
            ),
            addToBackStack = true
        )

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

        //Back Button
        mBinding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
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
            setSkippedUserData()
            goToHomePage()
        }


        mBinding.tvForgotPassword.setOnClickListener {
            replaceFragment(
                fragment = ForgotPasswordFragment.newInstance(),
                addToBackStack = true,
                bundle = null
            )
        }

        mBinding.layoutCaptcha.ivRefreshCaptchaCode.setOnClickListener {
            setUpCaptcha()
        }
    }

    //Method to redirect user to home page
    private fun goToHomePage() {
        startActivity(Intent(activity, BottomNavHomeActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })

        activity?.finish()
    }


    /*   FACEBOOK   */
    private fun facebookSign() {

        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK)

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
        FacebookSdk.setApplicationId(resources.getString(R.string.facebook_app_id))
        FacebookSdk.setIsDebugEnabled(true)
    }

    /*   GOOGLE   */
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
        var isValid = true
        mEnteredCaptch = mBinding.layoutCaptcha.etCaptcha.text?.toString()
        mViewModel.run {
            mBinding.run {
                if (!emailId.isEmailValid()) {
                    etEmailId.error = resources.getString(R.string.invalid_email)
                    isValid = false
                }
                if (password.trim().isEmpty()) {
                    etPassword.error = resources.getString(R.string.enter_password)
                    isValid = false
                }
                if (!password.isValidPassword()) {
                    etPassword.error = resources.getString(R.string.password_format)
                    isValid = false
                }
                if (mEnteredCaptch?.isEmpty()!!) {
                    layoutCaptcha.etCaptcha.error = resources.getString(R.string.empty_captcha)
                    isValid = false
                } else if (mEnteredCaptch != mCaptcha) {
                    layoutCaptcha.etCaptcha.error = resources.getString(R.string.invalid_captcha)
                    isValid = false
                }
            }
        }
        return isValid
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
            e.printStackTrace()
            showMessage(resources.getString(R.string.google_sign_failed), true)
        }
    }


}