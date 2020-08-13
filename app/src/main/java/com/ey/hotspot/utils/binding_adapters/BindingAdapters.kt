package com.ey.hotspot.utils.binding_adapters

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.ey.hotspot.R
import java.math.BigDecimal
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("bindSpeedValue")
fun bindSpeedValue(textView: TextView, value: BigDecimal?) {
    textView.text = value?.let {
        String.format(textView.context.getString(R.string.mbps_label), value)
    } ?: String.format(textView.context.getString(R.string.mbps_label), 0)
}

//Format Date Time from Calendar
@BindingAdapter("bindFormatDateTime")
fun bindFormatDateTime(textView: TextView, value: Calendar?) {
    textView.text = value?.let {
        SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").format(value.time)
    } ?: "-"
}

//Indicate when the WiFi is favourite
@BindingAdapter("bindAddFavourite")
fun bindAddFavourite(appCompatImageView: AppCompatImageView, isFavourite: Boolean) {
    if (isFavourite)
        appCompatImageView.setImageResource(R.drawable.ic_favorite_filled_red)
    else
        appCompatImageView.setImageResource(R.drawable.ic_favorite_filled_gray)
}

/**
 * This adapter will take a List & its message as input. If List is empty
 */
@BindingAdapter("bindShowTextViewListEmpty", "bindShowEmptyListMessage")
fun bindShowTextViewListEmpty(textView: TextView, list: List<Any>?, value: String?) {
    if (list.isNullOrEmpty()) {
        textView.visibility = View.VISIBLE
        textView.text = value
    } else {
        textView.visibility = View.GONE
    }
}

/**
 * This adapter will hide the views associated with its particular recyclerview if the list is empty
 */
@BindingAdapter("bindHideViewListEmpty")
fun bindHideViewListEmpty(view: View, list: List<Any>?) {
    if (list.isNullOrEmpty()) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("bindExtractDateFromDateTime")
fun bindExtractDateFromDateTime(textView: TextView, value: String?) {
    value?.let {
        textView.text = it.substringBefore("T")
    }
}

@BindingAdapter("bindExtractDateFromDateTime_DDMMYYYY")
fun bindExtractDateFromDateTimeDDMMYYYY(textView: TextView, value: String?) {
    value?.let {
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val outputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val date: Date = inputFormat.parse(it.substringBefore("T"))
        val outputDateStr: String = outputFormat.format(date)
        textView.text = outputDateStr
    }
}


@BindingAdapter("bindShowDashWhenTextEmpty")
fun showDashWhenTextEmpty2(textView: TextView, value: String?) {
    textView.text = value?.let {
        if (it.trim().isEmpty()) "-" else value
    } ?: "-"
}

@BindingAdapter("bindRatingView")
fun ratingView(textView: TextView, value: String?) {
    if (value.isNullOrEmpty())
        textView.visibility = View.GONE
    else {
        textView.visibility = View.VISIBLE
        textView.text = value
    }
}