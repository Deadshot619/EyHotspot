package com.ey.hotspot.ui.review_and_complaint.review_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.databinding.ItemReviewListBinding

class ReviewListAdapter(/*val listener: OnClickListener*/) :
    ListAdapter<ReviewListModel, ReviewListAdapter.ReviewListViewHolder>(
        DiffCallback
    ) {
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [ReviewListModel]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<ReviewListModel>() {
        override fun areItemsTheSame(
            oldItem: ReviewListModel,
            newItem: ReviewListModel
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: ReviewListModel,
            newItem: ReviewListModel
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
     * layout, which nicely gives it access to the full [ReviewListModel] information.
     */
    class ReviewListViewHolder(private var binding: ItemReviewListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: ReviewListModel?/*,
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
    class OnClickListener(val clickListener: (viewAvailableIndustriesData: ReviewListModel) -> Unit) {
        fun onClick(viewAvailableIndustriesData: ReviewListModel) =
            clickListener(viewAvailableIndustriesData)
    }

}