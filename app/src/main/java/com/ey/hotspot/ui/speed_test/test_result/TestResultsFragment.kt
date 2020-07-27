package com.ey.hotspot.ui.speed_test.test_result

import androidx.lifecycle.Observer
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentTestResultsBinding
import com.ey.hotspot.utils.constants.setUpToolbar
import java.math.BigDecimal

class TestResultsFragment : BaseFragment<FragmentTestResultsBinding, TestResultsViewModel>() {

    companion object {
        fun newInstance() = TestResultsFragment()

    }

    override fun getLayoutId() = R.layout.fragment_test_results
    override fun getViewModel() = TestResultsViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        activity?.setUpToolbar(
            mBinding.toolbarLayout,
            resources.getString(R.string.speed_test_label),
            true,
            showTextButton = true
        )

        setUpListeners()
        setUpObservers()
    }

    private fun setUpListeners(){
//        Toolbar retest button
        mBinding.toolbarLayout.tvTextButton.setOnClickListener {
            mViewModel.setSpeedValue(BigDecimal.valueOf(0))
            mViewModel.onCheckSpeedClick()
        }
    }

    private fun setUpObservers() {
        //Download Speed
        mViewModel.downloadSpeed.observe(viewLifecycleOwner, Observer {
            mBinding.imageSpeedometer.speedTo(it.toFloat(), 500)
        })
    }
}