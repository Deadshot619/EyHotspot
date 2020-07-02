package com.ey.hotspot.ui.speed_test.wifi_log_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.databinding.ItemWifiLogListBinding


class WifiLogListAdapter(/*val listener: OnClickListener*/) :
    ListAdapter<WifiLogListModel, WifiLogListAdapter.WifiLogListViewHolder>(
        DiffCallback
    ) {
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [WifiLogListModel]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<WifiLogListModel>() {
        override fun areItemsTheSame(
            oldItem: WifiLogListModel,
            newItem: WifiLogListModel
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: WifiLogListModel,
            newItem: WifiLogListModel
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiLogListViewHolder {
        return WifiLogListViewHolder(
            ItemWifiLogListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WifiLogListViewHolder, position: Int) {
        holder.bind(getItem(position) /*listener*/)
    }


    /**
     * The [WifiLogListViewHolder] constructor takes the binding variable from the associated
     * layout, which nicely gives it access to the full [WifiLogListModel] information.
     */
    class WifiLogListViewHolder(private var binding: ItemWifiLogListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: WifiLogListModel?/*,
            listener: OnClickListener*/
        ) {
            binding.run {
                data = item
//                clickListener = listener
//                tvNumber.text = "$count"
                executePendingBindings()
            }
        }

    }


    /**
     * Interface to call in the [OnClickListener] & passed on to fragment to implement
     */
    class OnClickListener(val clickListener: (viewAvailableIndustriesData: WifiLogListModel) -> Unit) {
        fun onClick(viewAvailableIndustriesData: WifiLogListModel) =
            clickListener(viewAvailableIndustriesData)
    }

}