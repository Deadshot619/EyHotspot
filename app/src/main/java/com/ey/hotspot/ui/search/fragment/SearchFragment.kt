package com.ey.hotspot.ui.search.fragment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentLoginSelectionBinding
import com.ey.hotspot.databinding.SearchFragmentBinding

class SearchFragment : BaseFragment<SearchFragmentBinding,SearchViewModel>() {
    override fun getLayoutId(): Int {

        return R.layout.search_fragment
    }

    override fun getViewModel(): Class<SearchViewModel> {

        return SearchViewModel::class.java
    }

    override fun onBinding() {
    }


}