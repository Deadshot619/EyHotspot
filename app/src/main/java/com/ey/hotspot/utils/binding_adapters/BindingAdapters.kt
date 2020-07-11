package com.ey.hotspot.utils.binding_adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ey.hotspot.R
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bindSpeedValue")
fun bindSpeedValue(textView: TextView, value: BigDecimal?){
    textView.text = value?.let {
        String.format(textView.context.getString(R.string.mbps_label), value)
    } ?:  String.format(textView.context.getString(R.string.mbps_label), 0)
}

//Format Date Time from Calendar
@BindingAdapter("bindFormatDateTime")
fun bindFormatDateTime(textView: TextView, value: Calendar?){
    textView.text = value?.let {
        SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(value.time)
    } ?: "-"
}