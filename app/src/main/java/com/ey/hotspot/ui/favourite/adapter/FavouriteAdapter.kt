package com.ey.hotspot.ui.favourite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ey.hotspot.R
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import kotlinx.android.synthetic.main.item_favourites_list.view.*

class FavouriteAdapter(var searchList: ArrayList<GetFavouriteItem>) :
    RecyclerView.Adapter<FavouriteAdapter.FavouriteListViewHolder>() {


    open fun updateUsers(newUsers: List<GetFavouriteItem>) {
        searchList.clear()
        searchList.addAll(newUsers)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteAdapter.FavouriteListViewHolder {
        return FavouriteListViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favourites_list, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    override fun onBindViewHolder(holder: FavouriteAdapter.FavouriteListViewHolder, position: Int) {
        holder.bind(searchList[position])

        holder.markFavourite.setOnClickListener {


        }

    }

    class FavouriteListViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val providerName = view.tv_wifi_ssid
        val name = view.tv_location
        val navigate = view.btn_navigate_now
        val markFavourite = view.iv_favourites
        fun bind(country: GetFavouriteItem) {

            providerName.text = country.provider_name
            name.text = country.name
            if (country.favourite) {
                markFavourite.setImageResource(R.drawable.ic_fill_heart)
            } else {
                markFavourite.setImageResource(R.drawable.ic_gray_heart)
            }
        }
    }
}