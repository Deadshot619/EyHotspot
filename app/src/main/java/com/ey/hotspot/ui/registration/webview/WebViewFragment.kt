package com.ey.hotspot.ui.registration.webview

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentWebViewBinding
import com.ey.hotspot.network.request.TermsRequest
import com.ey.hotspot.ui.home.BottomNavHomeActivity
import com.ey.hotspot.ui.login.permission.PermissionViewModel
import com.ey.hotspot.ui.registration.register_user.RegisterUserFragment
import com.ey.hotspot.ui.review_and_complaint.reviews.ReviewsFragment
import com.ey.hotspot.utils.extention_functions.removeFragment
import com.ey.hotspot.utils.extention_functions.showMessage

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
        fun newInstance(fragname: String) =
            WebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(FRAG_NAME, fragname)
                }
            }

        private const val FRAG_NAME = ""
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
            removeFragment(this)
        }

    }
    private fun setUpObservers() {

        //Social Login Response
        mViewModel.termsResponse.observe(viewLifecycleOwner, Observer {

            if (it.status) {
                showMessage(it.message, true)
                goToHomePage()

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
    //Method to redirect user to homepage
    private fun goToHomePage() {
        startActivity(Intent(activity, BottomNavHomeActivity::class.java).apply {
        })
        activity?.finish()
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