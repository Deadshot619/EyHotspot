package com.ey.hotspot.app_core_lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ey.hotspot.databinding.LayoutCustomToolbarBinding

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment(),
    UICallbacks<V> {

    protected lateinit var mBinding: T
    protected lateinit var mViewModel: V

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mViewModel = ViewModelProvider(this@BaseFragment).get(getViewModel())

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBinding()
    }

    /**
     * This method is used to get data from Fragment arguments
     */
    protected fun getDataFromArguments(context: Fragment, key: String): String {
        return context.arguments?.getString(key) ?: ""
        //Not a good way to do, due to tight coupling of fragment to activity
//        return ReportsPageActivity().visitReportId
    }

    protected fun setToolbar(
        toolbarBinding: LayoutCustomToolbarBinding,
        title: String,
        showUpButton: Boolean = false,
        endButtonTitle: String = "",
        showSettingButton: Boolean = false
    ){
            toolbarBinding.run {
                //Toolbar title
                tvTitle.text = title

                //If true, show Back Button, else hide it
                if (showUpButton) {
                    btnBack.visibility = View.VISIBLE
                    btnBack.setOnClickListener {
                        activity?.onBackPressed()
                    }
                } else {
                    btnBack.visibility = View.GONE
                }

//                Text Button
                tvTextButton.text = ""

//                Settings
                if (showSettingButton){
                    ivSettings.visibility = View.VISIBLE
                } else {
                    ivSettings.visibility = View.GONE
                }
            }
    }
}