package com.ey.hotspot.utils.binding_adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.ui.review_and_complaint.review_list.ReviewListAdapter
import com.ey.hotspot.ui.review_and_complaint.review_list.ReviewListModel
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListAdapter
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListModel

/**
 * When there is no Products List data (data is null), hide the [RecyclerView],
 * otherwise show it.
 */
@BindingAdapter("listReviewList")
fun bindReviewList(recyclerView: RecyclerView, data: List<ReviewListModel>?){
    val adapter = recyclerView.adapter as ReviewListAdapter
    adapter.submitList(data)
}

@BindingAdapter("listWifiLogList")
fun bindWifiLogList(recyclerView: RecyclerView, data: List<WifiLogListModel>?){
    val adapter = recyclerView.adapter as WifiLogListAdapter
    adapter.submitList(data)
}
