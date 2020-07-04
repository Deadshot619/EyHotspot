package com.ey.hotspot.ui.speed_test.speed_test_fragmet

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentSpeedTestBinding
import com.ey.hotspot.utils.constants.setUpToolbar

class SpeedTestFragment : BaseFragment<FragmentSpeedTestBinding, SpeedTestFragmentViewModel>() {

    companion object {
        fun newInstance() = SpeedTestFragment()
    }

    override fun getLayoutId() = R.layout.fragment_speed_test
    override fun getViewModel() = SpeedTestFragmentViewModel::class.java
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
    }

    fun setUpListeners() {
        //Go
//        mBinding.btnGo.setOnClickListener {
//            replaceFragment(TestResultsFragment(), true)
//        }
    }

}