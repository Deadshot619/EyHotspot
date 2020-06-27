package com.ey.hotspot.ui.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ey.hotspot.app_core_lib.BaseActivity

var toast: Toast? = null

fun Activity.showMessage(message: String) {
    if (toast != null){
        toast?.cancel()
    }
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}

fun Fragment.showMessage(message: String) {
    if (toast != null){
        toast?.cancel()
    }
    toast = Toast.makeText(this.activity, message, Toast.LENGTH_SHORT)
    toast?.show()
}


fun Fragment.addFragment(fragment: Fragment, addToBackStack: Boolean, bundle: Bundle? = null) {
    val activity = this.activity as BaseActivity<*, *>
    activity.addFragment(fragment, addToBackStack, bundle)
}

//Method to replace fragment in the container
fun Fragment.replaceFragment(fragment: Fragment, addToBackStack: Boolean, bundle: Bundle? = null) {
    val activity = this.activity as BaseActivity<*, *>
    activity.replaceFragment(fragment, addToBackStack, bundle)
}

//Method to remove fragment from backstack
fun Fragment.removeFragment(fragment: Fragment){
    val activity = this.activity as BaseActivity<*, *>
    activity.removeFragment(fragment)
}

fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view?.let {
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
