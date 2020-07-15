package com.ey.hotspot.app_core_lib

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.ey.hotspot.databinding.DialogProgressBinding

class LoadingDialog(context: Context) : AppCompatDialog(context) {

    private var mBinding: DialogProgressBinding =
        DialogProgressBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(mBinding.root)
        setCancelable(false)
        window?.setLayout(WRAP_CONTENT, WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun setMessage(msg: String) {
         mBinding.tvMessage.text = msg
    }
}