package com.ey.hotspot.ui.registration.webview

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentWebViewBinding
import com.ey.hotspot.network.request.TermsRequest
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.login.permission.PermissionViewModel
import com.ey.hotspot.utils.constants.updateSharedPreference
import com.ey.hotspot.utils.extention_functions.goToHomeScreen

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WebViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WebViewFragment() :
    BaseFragment<FragmentWebViewBinding, PermissionViewModel>() {

    companion object {
        fun newInstance(fragname: String, loginData: LoginResponse? = null) =
            WebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(FRAG_NAME, fragname)
                    putParcelable(LOGIN_DATA, loginData)
                }
            }

        private const val FRAG_NAME = ""
        private const val LOGIN_DATA = "login_data"
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_web_view
    }

    override fun getViewModel(): Class<PermissionViewModel> {
        return PermissionViewModel::class.java
    }

    override fun onBinding() {

        setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.terms_condition),
            showUpButton = false
        )
        setUpObservers()

        openWebview(mBinding.webview,"https://nearbyhs.php-dev.in/EyHotspots/public/acceptTermsConditions")
        mBinding.btnAgree.setOnClickListener {

           // HotSpotApp.prefs?.setTermsConditionStatus(true)
            val termsResponse: TermsRequest = TermsRequest(
                true
            )
            mViewModel.callTAndC(termsResponse)
            //removeFragment(this)
        }

    }
    private fun setUpObservers() {

        //terms and condition Response
        mViewModel.termsResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                //showMessage(it.message, true)
                arguments?.getParcelable<LoginResponse>(LOGIN_DATA)?.let { data ->
                    updateSharedPreference(data)
                }
                activity?.goToHomeScreen()

            }
        })

        if (arguments?.getString(FRAG_NAME).equals("register")) {
            mBinding.btnAgree.visibility=View.GONE
        }
        else
        {
            mBinding.btnAgree.visibility=View.VISIBLE
        }
    }

    private fun openWebview(webView: WebView, url:String)
    {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webView.loadUrl(url)
    }

}