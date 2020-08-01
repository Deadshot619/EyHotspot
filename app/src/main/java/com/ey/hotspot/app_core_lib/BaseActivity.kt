package com.ey.hotspot.app_core_lib

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.ey.hotspot.R
import com.ey.hotspot.utils.LanguageManager
import com.ey.hotspot.utils.MyHotSpotSharedPreference
import com.ey.hotspot.utils.extention_functions.showMessage

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel> : AppCompatActivity(),
    UICallbacks<V> {

    private var doubleBackToExitPressedOnce = false

    protected lateinit var mBinding: T
    protected lateinit var mViewModel: V
    protected lateinit var mContext: Context

    //  protected lateinit var mPref: PreferencesHelper
    private lateinit var mManager: FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager.setLanguage(this@BaseActivity, HotSpotApp.prefs!!.getLanguage())

        mBinding = DataBindingUtil.setContentView(this@BaseActivity, getLayoutId())
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mViewModel = ViewModelProvider(this@BaseActivity).get(getViewModel())

        mManager = supportFragmentManager
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mContext = this@BaseActivity
//        mPref = PreferencesHelper
//        createDialog()
        onBinding()
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
    fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            remove(fragment)
            commit()
        }
        supportFragmentManager.popBackStack()
    }

    fun restartApplication(context: Context?, thobeSharedPreference: MyHotSpotSharedPreference) {
        LanguageManager.setLanguage(context!!, thobeSharedPreference.getLanguage())
        /**
         * restart the application for API > 25 otherwise just recreate
         */
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
            restartApp()
        } else {
            /**
             * pop back fragment before activity recreating
             */

            recreate()
        }
    }

    fun restartApp() {
        val i = baseContext.packageManager
            .getLaunchIntentForPackage(baseContext.packageName)
        i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
        startActivity(i)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        showMessage(getString(R.string.press_back_again_label))

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)

    }


    open fun checkLocSaveState(): Boolean {

        var status: Boolean = false
        val lm =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        try {
            network_enabled = lm != null && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        if (gps_enabled && network_enabled) {
            status = true
        } else if (gps_enabled && network_enabled) {
            status = false
        } else if (gps_enabled && network_enabled) {
            status = false
        } else if (gps_enabled && network_enabled) {
            status = false
        }
        return status
    }

    fun clearFragmentBackstack(){
        for(i in 0..mManager.backStackEntryCount)
            mManager.popBackStack()
    }
}
