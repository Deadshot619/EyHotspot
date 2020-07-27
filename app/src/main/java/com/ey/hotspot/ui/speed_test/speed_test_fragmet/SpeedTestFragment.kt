package com.ey.hotspot.ui.speed_test.speed_test_fragmet

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentSpeedTestBinding
import com.ey.hotspot.ui.speed_test.test_result.TestResultsFragment
import com.ey.hotspot.utils.constants.Constants
import com.ey.hotspot.utils.constants.setUpToolbar
import com.ey.hotspot.utils.extention_functions.getUserLocation
import com.ey.hotspot.utils.extention_functions.replaceFragment

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
            false,
            showTextButton = false
        )

        setUpListeners()

        activity?.getUserLocation { lat, lng ->
           if (lat != null && lng != null)
               mViewModel.verifyHotspot(lat, lng)
            else
               mViewModel.verifyHotspot(Constants.LATITUDE, Constants.LONGITUDE)
        }
    }

    fun setUpListeners() {
        //Go
        mBinding.tvGo.setOnClickListener {
            replaceFragment(TestResultsFragment(), true)
        }
    }



}