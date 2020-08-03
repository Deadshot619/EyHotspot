package com.ey.hotspot.ui.registration.webview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.app_core_lib.HotSpotApp
import com.ey.hotspot.databinding.CompleteRegistrationFragmentBinding
import com.ey.hotspot.databinding.FragmentPermissionBinding
import com.ey.hotspot.databinding.FragmentWebViewBinding
import com.ey.hotspot.ui.login.permission.PermissionViewModel
import com.ey.hotspot.ui.registration.email_verification.CompleteRegistrationFragment
import com.ey.hotspot.ui.registration.email_verification.CompleteRegistrationViewModel
import com.ey.hotspot.utils.extention_functions.removeFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WebViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WebViewFragment :
    BaseFragment<FragmentWebViewBinding, PermissionViewModel>() {


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

        openWebview(mBinding.webview,"https://nearbyhs.php-dev.in/EyHotspots/public/acceptTermsConditions")

        mBinding.btnAgree.setOnClickListener {

            HotSpotApp.prefs?.setTermsConditionStatus(true)

            removeFragment(this)
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
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WebViewFragment().apply {

            }
    }
}