package com.ey.hotspot.ui.favourite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.ui.search.searchlist.model.SearchList

class FavouriteViewModel(application: Application) : BaseViewModel(application) {


    private val _favouriteWifiList = MutableLiveData<List<SearchList>>(
        listOf(
            SearchList("Avator", "prashantj@gmail.com", "prashant", 1, "KK"),
            SearchList("Avator", "prashantj@gmail.com", "prashant", 2, "KK")
        )

    )
    val favouriteWifiList: LiveData<List<SearchList>>
        get() = _favouriteWifiList


}