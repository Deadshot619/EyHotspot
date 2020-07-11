package com.ey.hotspot.ui.speed_test.raise_complaint

import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseFragment
import com.ey.hotspot.databinding.FragmentRaiseComplaintBinding

class RaiseComplaintFragment : BaseFragment<FragmentRaiseComplaintBinding, RaiseComplaintViewModel>() {

    companion object {
        fun newInstance() = RaiseComplaintFragment()
    }

    override fun getLayoutId() = R.layout.fragment_raise_complaint
    override fun getViewModel() = RaiseComplaintViewModel::class.java
    override fun onBinding() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = mViewModel

        setUpToolbar(toolbarBinding = mBinding.toolbarLayout, title = getString(R.string.comment_and_evaluate_label), showUpButton = true)

    }


}