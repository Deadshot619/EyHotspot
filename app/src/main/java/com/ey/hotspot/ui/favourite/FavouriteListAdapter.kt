package com.ey.hotspot.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.databinding.ItemFavouritesListBinding
import com.ey.hotspot.ui.search.searchlist.model.SearchList


class FavouriteListAdapter(val listener: OnClickListener) :
    ListAdapter<SearchList, FavouriteListAdapter.FavouriteListViewHolder>(
        DiffCallback
    ) {
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [SearchList]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<SearchList>() {
        override fun areItemsTheSame(
            oldItem: SearchList,
            newItem: SearchList
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: SearchList,
            newItem: SearchList
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteListViewHolder {
        return FavouriteListViewHolder(
            ItemFavouritesListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavouriteListViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }


    /**
     * The [FavouriteListViewHolder] constructor takes the binding variable from the associated
     * layout, which nicely gives it access to the full [SearchList] information.
     */
    class FavouriteListViewHolder(private var binding: ItemFavouritesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: SearchList?,
            listener: OnClickListener
        ) {
            binding.run {
//                data = item
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
        fun onClickRateNow()
    }

}