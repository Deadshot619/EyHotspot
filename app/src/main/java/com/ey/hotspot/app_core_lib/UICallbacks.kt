package com.ey.hotspot.app_core_lib

import androidx.annotation.LayoutRes

interface UICallbacks<V> {

    @LayoutRes
    fun getLayoutId(): Int
    fun getViewModel(): Class<V>
    fun onBinding()
}