package com.ey.hotspot.ui.review_and_complaint.complaint_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.databinding.ItemComplaintListBinding
import com.ey.hotspot.network.response.ComplaintsList

class ComplaintListAdapter(/*val listener: OnClickListener*/) :
    ListAdapter<ComplaintsList, ComplaintListAdapter.ComplaintListViewHolder>(
        DiffCallback
    ) {

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [ComplaintsList]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<ComplaintsList>() {
        override fun areItemsTheSame(
            oldItem: ComplaintsList,
            newItem: ComplaintsList
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ComplaintsList,
            newItem: ComplaintsList
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintListViewHolder {
        return ComplaintListViewHolder(
            ItemComplaintListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ComplaintListViewHolder, position: Int) {
        holder.bind(getItem(position) /*listener*/)
    }


    /**
     * The [ComplaintListViewHolder] constructor takes the binding variable from the associated
     * layout, which nicely gives it access to the full [ComplaintsList] information.
     */
    class ComplaintListViewHolder(private var binding: ItemComplaintListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: ComplaintsList?/*,
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
    class OnClickListener(val clickListener: (viewAvailableIndustriesData: ComplaintsList) -> Unit) {
        fun onClick(viewAvailableIndustriesData: ComplaintsList) =
            clickListener(viewAvailableIndustriesData)
    }

}