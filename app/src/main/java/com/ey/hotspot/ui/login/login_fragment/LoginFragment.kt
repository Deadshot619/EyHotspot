package com.ey.hotspot.ui.login.login_fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginBinding
import com.ey.hotspot.ui.favourite.fragment.FavouriteFragment
import com.ey.hotspot.ui.home.HomeActivity
import com.ey.hotspot.ui.login.LoginViewModel
import com.ey.hotspot.ui.registration.email_verification.EmailVerificationFragment
import com.ey.hotspot.ui.search.searchlist.SearchListFragment
import com.ey.hotspot.ui.search.submitcomplaint.SubmitComplaintFragment
import com.ey.hotspot.utils.replaceFragment


class LoginFragment :BaseFragment<FragmentLoginBinding,LoginFragmentViewModel>() {


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

        mBinding.btSubmit.setOnClickListener {

         /*   val homeIntent = Intent(activity,HomeActivity::class.java)
            startActivity(homeIntent)*/

           // replaceFragment(SearchListFragment.newInstance(),true,null)
           // replaceFragment(FavouriteFragment.newInstance(),true,null)
            replaceFragment(SubmitComplaintFragment.newInstance(),true,null)
        }
    }

}