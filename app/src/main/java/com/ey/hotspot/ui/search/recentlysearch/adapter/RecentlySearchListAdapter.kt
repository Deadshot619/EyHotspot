package com.ey.hotspot.ui.search.recentlysearch.adapter

//import kotlinx.android.synthetic.main.list_item_search_list.view.*view
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.ui.search.recentlysearch.model.RecentlySearch
import kotlinx.android.synthetic.main.list_recently_search_item.view.*

class RecentlySearchListAdapter(var searchList: ArrayList<RecentlySearch>) :
    RecyclerView.Adapter<RecentlySearchListAdapter.RecentlySearchViewHolder>() {


    open fun updateUsers(newUsers: List<RecentlySearch>) {
        searchList.clear()
        searchList.addAll(newUsers)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentlySearchListAdapter.RecentlySearchViewHolder {
        return RecentlySearchViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_recently_search_item, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(
        holder: RecentlySearchListAdapter.RecentlySearchViewHolder,
        position: Int
    ) {
        holder.bind(searchList[position])

    }

    class RecentlySearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val userName = view.tv_suggestion_item_title
//        private val userEmail = view.tvOwnerName
        fun bind(country: RecentlySearch) {
            userName.text = country.firstName

        }
    }
}