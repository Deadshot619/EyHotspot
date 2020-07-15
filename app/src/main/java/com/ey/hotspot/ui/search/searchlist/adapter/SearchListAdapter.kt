package com.ey.hotspot.ui.search.searchlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.ui.search.searchlist.model.SearchList
import kotlinx.android.synthetic.main.item_search_list.view.*

class SearchListAdapter(var searchList:ArrayList<SearchList>):RecyclerView.Adapter<SearchListAdapter.SearchListViewHolder>() {


   open fun updateUsers(newUsers: List<SearchList>) {
        searchList.clear()
        searchList.addAll(newUsers)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): SearchListAdapter.SearchListViewHolder {
        return SearchListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_list, parent, false))

    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: SearchListAdapter.SearchListViewHolder, position: Int) {
        holder.bind(searchList[position])

    }

    class SearchListViewHolder(view: View) :RecyclerView.ViewHolder(view){


        private val userName = view.tv_wifi_ssid
        private val userEmail = view.tv_location
        fun bind(country: SearchList) {
            userName.text = country.firstName + " " + country.lastName
            userEmail.text = country.email
        }
    }
}