package com.ey.hotspot.ui.review_and_complaint.reviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.databinding.ItemReviewsBinding
import com.ey.hotspot.network.response.LocationReviews

class ReviewsAdapter(/*val listener: OnClickListener*/) :
    ListAdapter<LocationReviews, ReviewsAdapter.ReviewsViewHolder>(
        DiffCallback
    ) {
    
    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [LocationReviews]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<LocationReviews>() {
        override fun areItemsTheSame(
            oldItem: LocationReviews,
            newItem: LocationReviews
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: LocationReviews,
            newItem: LocationReviews
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        return ReviewsViewHolder(
            ItemReviewsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        holder.bind(getItem(position) /*listener*/)
    }


    /**
     * The [ReviewsViewHolder] constructor takes the binding variable from the associated
     * layout, which nicely gives it access to the full [LocationReviews] information.
     */
    class ReviewsViewHolder(private var binding: ItemReviewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: LocationReviews?/*,
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
    class OnClickListener(val clickListener: (viewAvailableIndustriesData: LocationReviews) -> Unit) {
        fun onClick(viewAvailableIndustriesData: LocationReviews) =
            clickListener(viewAvailableIndustriesData)
    }

}