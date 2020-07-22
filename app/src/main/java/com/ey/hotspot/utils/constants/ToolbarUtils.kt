package com.ey.hotspot.utils.constants

import android.app.Activity
import android.view.View
import android.view.inputmethod.EditorInfo
import com.ey.hotspot.R
import com.ey.hotspot.databinding.LayoutCustomToolbarDarkBinding
import com.ey.hotspot.databinding.LayoutCustomToolbarSearchbarBinding
import com.ey.hotspot.utils.showKeyboard
import com.ey.hotspot.utils.showMessage

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

fun Activity.setUpSearchBar(
    toolbarBinding: LayoutCustomToolbarSearchbarBinding,
    showUpButton: Boolean = true,
    enableSearchButton: Boolean = true,
    showShadow: Boolean = true,
    searchFunction: (String) -> Unit    //method to run when search button is clicked
) {
    toolbarBinding.run {
        //If true, show Back Button, else hide it
        if (showUpButton) {
            ivBack.visibility = View.VISIBLE
            ivBack.setOnClickListener {
                this@setUpSearchBar.onBackPressed()
            }
        } else {    //If false, hide up button & lower the padding of edittext
            ivBack.visibility = View.GONE
            etSearchBar.setPaddingRelative(
                resources.getDimensionPixelSize(R.dimen.search_bar_padding_start_no_drawable),
                etSearchBar.paddingTop,
                resources.getDimensionPixelSize(R.dimen.search_bar_padding),
                etSearchBar.paddingTop
            )
        }

        if (enableSearchButton){
            //Search button
            ivSearch.setOnClickListener {
                if (etSearchBar.text.isNullOrEmpty()) {
                    showMessage(resources.getString(R.string.empty_query_alert_label))
                    etSearchBar.requestFocus()
                    this@setUpSearchBar?.showKeyboard()
                } else {
                    searchFunction(etSearchBar.text.toString().trim())
                }
            }

            etSearchBar.setOnEditorActionListener{_, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO){
                    if (etSearchBar.text.isNullOrEmpty()) {
                        showMessage(resources.getString(R.string.empty_query_alert_label))
                        etSearchBar.requestFocus()
                        this@setUpSearchBar?.showKeyboard()
                    } else {
                        searchFunction(etSearchBar.text.toString().trim())
                    }
                }
                true
            }
        }

        //Shadow
        if (showShadow)
            toolbar.elevation = 10f
        else
            toolbar.elevation = 0f

    }
}