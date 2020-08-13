package com.ey.hotspot.ui.registration.webview

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.FragmentWebViewBinding
import com.ey.hotspot.network.request.TermsRequest
import com.ey.hotspot.network.response.LoginResponse
import com.ey.hotspot.ui.login.permission.PermissionViewModel
import com.ey.hotspot.utils.IOnBackPressed
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.setUpToolbar
import com.ey.hotspot.utils.constants.updateSharedPreference
import com.ey.hotspot.utils.extention_functions.goToHomeScreen
import kotlinx.android.synthetic.main.fragment_web_view.*

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
    BaseFragment<FragmentWebViewBinding, PermissionViewModel>(), IOnBackPressed {

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
        var back:Boolean=false
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_web_view
    }

    override fun getViewModel(): Class<PermissionViewModel> {
        return PermissionViewModel::class.java
    }

    override fun onBinding() {

       /* setUpToolbar(
            toolbarBinding = mBinding.toolbarLayout,
            title = getString(R.string.terms_condition),
            showUpButton = false
        )*/

        if (arguments?.getString(FRAG_NAME).equals("register")) {
            activity?.setUpToolbar(
                mBinding.toolbarLayout,
                getString(R.string.terms_condition),
                true
            )
            mBinding.btnAgree.visibility=View.GONE
            back=true
        }
        else
        {
            activity?.setUpToolbar(
                mBinding.toolbarLayout,
                getString(R.string.terms_condition),
                false,
                showTextButton = false
            )
            mBinding.btnAgree.visibility=View.VISIBLE
            mBinding.toolbarLayout.btnBack.setOnClickListener {
                back=true
            }
            back=false
        }
        setUpObservers()

        val langType = HotSpotApp.prefs!!.getLanguage()
        if (langType == Constants.ENGLISH_LANG) {
            openWebview(mBinding.webview,Constants.TANDC_UR_ENGLISH)
        } else if (langType == Constants.ARABIC_LANG) {
            openWebview(mBinding.webview,Constants.TANDC_UR_ARABIC)
        }
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


    }



    private fun openWebview(webView: WebView, url:String)
    {
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        webView.loadUrl(url)
    }

    override fun onBackPressed(): Boolean {
        return back
    }

}