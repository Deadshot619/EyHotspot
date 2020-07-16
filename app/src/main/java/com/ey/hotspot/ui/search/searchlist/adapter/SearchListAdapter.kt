package com.ey.hotspot.ui.search.searchlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.databinding.ItemSearchListBinding
import com.ey.hotspot.ui.home.models.GetUserHotSpotResponse


class SearchListAdapter(val listener: OnClickListener) :
    ListAdapter<GetUserHotSpotResponse, SearchListAdapter.SearchListViewHolder>(
        DiffCallback
    ) {
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [GetUserHotSpotResponse]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<GetUserHotSpotResponse>() {
        override fun areItemsTheSame(
            oldItem: GetUserHotSpotResponse,
            newItem: GetUserHotSpotResponse
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: GetUserHotSpotResponse,
            newItem: GetUserHotSpotResponse
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListViewHolder {
        return SearchListViewHolder(
            ItemSearchListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchListViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }


    /**
     * The [SearchListViewHolder] constructor takes the binding variable from the associated
     * layout, which nicely gives it access to the full [GetUserHotSpotResponse] information.
     */
    class SearchListViewHolder(private var binding: ItemSearchListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: GetUserHotSpotResponse?,
            listener: OnClickListener
        ) {
            binding.run {
                data = item
                clickListener = listener
//                tvNumber.text = "$count"
                executePendingBindings()
            }
        }

    }



    /**
     * Interface to call in the [OnClickListener] & passed on to fragment to implement
     */
    interface OnClickListener{
        fun onClickRateNow(data: GetUserHotSpotResponse)
        fun onClickReport(data: GetUserHotSpotResponse)
        fun onClickAddFavourite(data: GetUserHotSpotResponse)
        fun onClickNavigate(data: GetUserHotSpotResponse)
    }


}