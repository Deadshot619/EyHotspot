package com.ey.hotspot.ui.favourite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.ui.search.searchlist.model.SearchList
import kotlinx.android.synthetic.main.item_favourites_list.view.*

class FavouriteAdapter(var searchList:ArrayList<SearchList>):
    RecyclerView.Adapter<FavouriteAdapter.FavouriteListViewHolder>() {


    open fun updateUsers(newUsers: List<SearchList>) {
        searchList.clear()
        searchList.addAll(newUsers)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): FavouriteAdapter.FavouriteListViewHolder {
        return FavouriteListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favourites_list, parent, false))

    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: FavouriteAdapter.FavouriteListViewHolder, position: Int) {
        holder.bind(searchList[position])

    }

    class FavouriteListViewHolder(view: View) : RecyclerView.ViewHolder(view){


        private val userName = view.tv_wifi_ssid
        private val userEmail = view.tv_username
        fun bind(country: SearchList) {
            userName.text = country.firstName + " " + country.lastName
            userEmail.text = country.email
        }
    }
}