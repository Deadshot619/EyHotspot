package com.ey.hotspot.ui.speed_test.wifi_log_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.database.WifiInformationTable
import com.ey.hotspot.databinding.ItemWifiLogListBinding


class WifiLogListAdapter(val listener: OnClickListener) :
    ListAdapter<WifiInformationTable, WifiLogListAdapter.WifiLogListViewHolder>(
        DiffCallback
    ) {
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [WifiInformationTable]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<WifiInformationTable>() {
        override fun areItemsTheSame(
            oldItem: WifiInformationTable,
            newItem: WifiInformationTable
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: WifiInformationTable,
            newItem: WifiInformationTable
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
        holder.bind(getItem(position), listener)
    }


    /**
     * The [WifiLogListViewHolder] constructor takes the binding variable from the associated
     * layout, which nicely gives it access to the full [WifiInformationTable] information.
     */
    class WifiLogListViewHolder(private var binding: ItemWifiLogListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: WifiInformationTable?,
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
    class OnClickListener(val clickListener: (data: WifiInformationTable) -> Unit) {
        fun onClick(data: WifiInformationTable) =
            clickListener(data)
    }

}