package com.ey.hotspot.utils.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.ey.hotspot.databinding.DialogOkBinding

class OkDialog(context: Context) : AppCompatDialog(context) {

    private var mBinding = DialogOkBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(mBinding.root)
        setCancelable(false)
//        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun setViews(
        title: String? = null,
        okBtn: ((Unit) -> Unit)? = null
    ) {

        title?.let { mBinding.tvTitle.text = it }
        okBtn?.let {
            mBinding.btnOk.setOnClickListener {
                okBtn(Unit)
            }
        }

    }
}
