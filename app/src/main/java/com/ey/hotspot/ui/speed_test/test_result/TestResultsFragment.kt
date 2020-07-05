package com.ey.hotspot.ui.speed_test.test_result

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentTestResultsBinding
import com.github.anastr.speedviewlib.components.indicators.ImageIndicator

class TestResultsFragment : BaseFragment<FragmentTestResultsBinding, TestResultsViewModel>() {

    companion object {
        fun newInstance() = TestResultsFragment()
    }

    override fun getLayoutId() = R.layout.fragment_test_results
    override fun getViewModel() = TestResultsViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel


        val imageIndicator = ImageIndicator(requireContext(), resources.getDrawable(R.drawable.speed_indicator_bitmap, null))
        mBinding.imageSpeedometer.indicator = imageIndicator
        mBinding.imageSpeedometer.speedTo(80f)
    }
}