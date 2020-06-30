package com.ey.hotspot.utils.binding_adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.ui.review_and_complaint.review_list.ReviewListAdapter
import com.ey.hotspot.ui.review_and_complaint.review_list.ReviewListModel

/**
 * When there is no Products List data (data is null), hide the [RecyclerView],
 * otherwise show it.
 */
@BindingAdapter("listReviewList")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ReviewListModel>?){
    val adapter = recyclerView.adapter as ReviewListAdapter
    adapter.submitList(data)
}
