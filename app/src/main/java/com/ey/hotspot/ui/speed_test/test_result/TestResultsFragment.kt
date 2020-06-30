package com.ey.hotspot.ui.speed_test.test_result

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentTestResultsBinding

class TestResultsFragment : BaseFragment<FragmentTestResultsBinding, TestResultsViewModel>() {

    companion object {
        fun newInstance() = TestResultsFragment()
    }

    override fun getLayoutId() = R.layout.fragment_test_results
    override fun getViewModel() = TestResultsViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel


    }
}