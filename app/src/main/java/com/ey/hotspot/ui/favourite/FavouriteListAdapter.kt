package com.ey.hotspot.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.databinding.ItemFavouritesListBinding
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem


class FavouriteListAdapter(val listener: OnClickListener) :
    ListAdapter<GetFavouriteItem, FavouriteListAdapter.FavouriteListViewHolder>(
        DiffCallback
    ) {
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [GetFavouriteItem]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<GetFavouriteItem>() {
        override fun areItemsTheSame(
            oldItem: GetFavouriteItem,
            newItem: GetFavouriteItem
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: GetFavouriteItem,
            newItem: GetFavouriteItem
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
     * layout, which nicely gives it access to the full [GetFavouriteItem] information.
     */
    class FavouriteListViewHolder(private var binding: ItemFavouritesListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: GetFavouriteItem?,
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
        fun onClickRateNow()
    }

}