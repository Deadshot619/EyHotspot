package com.ey.hotspot.ui.review_and_complaint.review_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.databinding.ItemReviewListBinding
import com.ey.hotspot.network.response.ReviewsList

class ReviewListAdapter(/*val listener: OnClickListener*/) :
    ListAdapter<ReviewsList, ReviewListAdapter.ReviewListViewHolder>(
        DiffCallback
    ) {
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [ReviewsList]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<ReviewsList>() {
        override fun areItemsTheSame(
            oldItem: ReviewsList,
            newItem: ReviewsList
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ReviewsList,
            newItem: ReviewsList
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListViewHolder {
        return ReviewListViewHolder(
            ItemReviewListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewListViewHolder, position: Int) {
        holder.bind(getItem(position) /*listener*/)
    }


    /**
     * The [ReviewListViewHolder] constructor takes the binding variable from the associated
     * layout, which nicely gives it access to the full [ReviewsList] information.
     */
    class ReviewListViewHolder(private var binding: ItemReviewListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: ReviewsList?/*,
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
    class OnClickListener(val clickListener: (viewAvailableIndustriesData: ReviewsList) -> Unit) {
        fun onClick(viewAvailableIndustriesData: ReviewsList) =
            clickListener(viewAvailableIndustriesData)
    }

}