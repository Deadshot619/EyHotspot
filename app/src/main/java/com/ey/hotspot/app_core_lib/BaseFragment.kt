package com.ey.hotspot.app_core_lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ey.hotspot.databinding.LayoutCustomToolbarBinding
import com.ey.hotspot.utils.dialogs.LoadingDialog
import com.ey.hotspot.utils.extention_functions.showMessage

abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment(),
    UICallbacks<V> {

    protected lateinit var mBinding: T
    protected lateinit var mViewModel: V
    //Custom Loading Dialog
    private val dialog: LoadingDialog by lazy {
        LoadingDialog(
            requireContext()
        )
    }

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

        setUpObservers()
        onBinding()
    }

    /**
     * Method to set up Observers
     */
    private fun setUpObservers(){
//        Error Text
        mViewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {  msg ->
                showMessage(msg, mViewModel.toastMessageDuration)
            }
        })

        //Dialog Visibility
        mViewModel.dialogVisibility.observe(viewLifecycleOwner, Observer { show ->
            dialog.run {
                if (show) show() else dismiss()
            }
        })

        //Dialog Message
        mViewModel.dialogMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                dialog.setMessage(it)
            }
        })
    }

    /**
     * This method is used to get data from Fragment arguments
     */
    protected fun getDataFromArguments(context: Fragment, key: String): String {
        return context.arguments?.getString(key) ?: ""
        //Not a good way to do, due to tight coupling of fragment to activity
//        return ReportsPageActivity().visitReportId
    }

    protected fun setUpToolbar(
        toolbarBinding: LayoutCustomToolbarBinding,
        title: String,
        showUpButton: Boolean = false,
        showSettingButton: Boolean = false,
        showShadow: Boolean = true
    ) {
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
                btnBack.visibility = View.INVISIBLE
            }

            //Settings
            if (showSettingButton) {
                ivSettings.visibility = View.VISIBLE
            } else {
                ivSettings.visibility = View.GONE
            }

            //Shadow
            if (showShadow)
                toolbar.elevation = 10f
            else
                toolbar.elevation = 0f
        }
    }
}