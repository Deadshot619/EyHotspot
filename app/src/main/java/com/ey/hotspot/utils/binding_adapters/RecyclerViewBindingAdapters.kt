package com.ey.hotspot.utils.binding_adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.database.WifiInformationTable
import com.ey.hotspot.network.response.ComplaintsList
import com.ey.hotspot.network.response.ReviewsList
import com.ey.hotspot.ui.favourite.FavouriteListAdapter
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.review_and_complaint.review_list.ReviewListAdapter
import com.ey.hotspot.ui.search.searchlist.adapter.SearchListAdapter
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListAdapter

/**
 * When there is no data (data is null), hide the [RecyclerView],
 * otherwise show it.
 */

//Review List
@BindingAdapter("listReviewList")
fun bindReviewList(recyclerView: RecyclerView, data: List<ReviewsList>?){
    val adapter = recyclerView.adapter as ReviewListAdapter
    adapter.submitList(data)
}

//Complaints List
@BindingAdapter("listComplaintList")
fun bindComplaintList(recyclerView: RecyclerView, data: List<ComplaintsList>?){
    val adapter = recyclerView.adapter /*as ReviewListAdapter*/
//    adapter.submitList(data)
}

//Wifi Log List
@BindingAdapter("listWifiLogList")
fun bindWifiLogList(recyclerView: RecyclerView, data: List<WifiInformationTable>?){
    val adapter = recyclerView.adapter as WifiLogListAdapter
    adapter.submitList(data)
}

//Favourite List
@BindingAdapter("listFavouriteWifiList")
fun bindFavouriteWifiList(recyclerView: RecyclerView, data: List<GetFavouriteItem>?){
    val adapter = recyclerView.adapter as FavouriteListAdapter
    adapter.submitList(data)
}

//Search List
@BindingAdapter("listSearchWifiList")
fun bindSearchWifiList(recyclerView: RecyclerView, data: List<GetHotSpotResponse>?){
    val adapter = recyclerView.adapter as SearchListAdapter
    adapter.submitList(data)
}

