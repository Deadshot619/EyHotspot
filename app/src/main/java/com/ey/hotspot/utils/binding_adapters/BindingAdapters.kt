package com.ey.hotspot.utils.binding_adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ey.hotspot.R
import java.math.BigDecimal

@BindingAdapter("bindSpeedValue")
fun bindSpeedValue(textView: TextView, value: BigDecimal?){
    textView.text = value?.let {
        String.format(textView.context.getString(R.string.mbps_label), value)
    } ?:  String.format(textView.context.getString(R.string.mbps_label), 0)
}