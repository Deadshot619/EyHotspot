package com.ey.hotspot.ui.search.searchlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.databinding.ItemSearchListBinding
import com.ey.hotspot.ui.home.models.GetHotSpotResponse


class SearchListAdapter(val listener: OnClickListener) :
    ListAdapter<GetHotSpotResponse, SearchListAdapter.SearchListViewHolder>(
        DiffCallback
    ) {
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [GetHotSpotResponse]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<GetHotSpotResponse>() {
        override fun areItemsTheSame(
            oldItem: GetHotSpotResponse,
            newItem: GetHotSpotResponse
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: GetHotSpotResponse,
            newItem: GetHotSpotResponse
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
     * layout, which nicely gives it access to the full [GetHotSpotResponse] information.
     */
    class SearchListViewHolder(private var binding: ItemSearchListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: GetHotSpotResponse?,
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
        fun onClickRateNow(data: GetHotSpotResponse)
        fun onClickReport(data: GetHotSpotResponse)
        fun onClickAddFavourite(data: GetHotSpotResponse)
        fun onClickNavigate(data: GetHotSpotResponse)
    }


}