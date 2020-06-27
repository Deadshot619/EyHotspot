package com.ey.hotspot.app_core_lib

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.ey.hotspot.R

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(),
    UICallbacks<V> {

    protected lateinit var mBinding: T
    protected lateinit var mViewModel: V
    protected lateinit var mContext: Context
//    protected lateinit var mPref: PreferencesHelper
    private lateinit var mManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this@BaseActivity, getLayoutId())
        mViewModel = ViewModelProvider(this@BaseActivity).get(getViewModel())
        mManager = supportFragmentManager
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mContext = this@BaseActivity
//        mPref = PreferencesHelper
//        createDialog()
        onBinding()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    fun addFragment(fragment: Fragment, addToBackstack: Boolean, bundle: Bundle? = null) {
        bundle?.let {
            fragment.arguments = bundle
        }
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, fragment)
            if (addToBackstack) {
                addToBackStack(fragment::class.java.simpleName)
            }
            commit()
        }
    }

    /**
     * Method to replace fragment in the container
     */
    fun replaceFragment(fragment: Fragment, addToBackstack: Boolean, bundle: Bundle? = null) {
        bundle?.let {
            fragment.arguments = bundle
        }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            if (addToBackstack) {
                addToBackStack(fragment::class.java.simpleName)
            }
            commit()
        }
    }

    /**
     * Method to remove fragment from backStack
     */
    fun removeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            remove(fragment)
            commit()
        }
        supportFragmentManager.popBackStack()
    }
}
