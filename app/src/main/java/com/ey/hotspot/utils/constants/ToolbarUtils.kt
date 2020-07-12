package com.ey.hotspot.utils.constants

import android.app.Activity
import android.view.View
import com.ey.hotspot.databinding.LayoutCustomToolbarDarkBinding

fun Activity.setUpToolbar(
    toolbarBinding: LayoutCustomToolbarDarkBinding,
    title: String,
    showUpButton: Boolean = false,
    showTextButton: Boolean = false
) {
    toolbarBinding.run {
        //Toolbar title
        tvTitle.text = title

        //If true, show Back Button, else hide it
        if (showUpButton) {
            btnBack.visibility = View.VISIBLE
            btnBack.setOnClickListener {
                this@setUpToolbar.onBackPressed()

            }
        } else {
            btnBack.visibility = View.INVISIBLE
        }

        //If true, show Text Button, else hide it
        if (showTextButton) {
            tvTextButton.visibility = View.VISIBLE
            tvTextButton.setOnClickListener {
//                this@setUpToolbar.onBackPressed()
            }
        } else {
            tvTextButton.visibility = View.INVISIBLE
        }
    }
}