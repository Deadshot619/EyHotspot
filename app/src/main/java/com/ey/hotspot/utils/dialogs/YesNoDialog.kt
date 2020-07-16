package com.ey.hotspot.utils.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatDialog
import com.ey.hotspot.databinding.DialogYesNoBinding

class YesNoDialog(context: Context) : AppCompatDialog(context) {

    private var mBinding = DialogYesNoBinding.inflate(LayoutInflater.from(context))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(mBinding.root)
        setCancelable(false)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun setViews(
        title: String,
        description: String,
        yes: (Unit) -> Unit,
        no: (Unit) -> Unit
    ) {
        mBinding.run {
            tvTitle.text = title
            tvDescription.text = description
            btnYes.setOnClickListener {
                yes(Unit)
            }
            btnNo.setOnClickListener {
                no(Unit)
            }
        }
    }
}