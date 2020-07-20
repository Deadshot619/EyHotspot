package com.ey.hotspot.ui.favourite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import com.ey.hotspot.ui.search.searchlist.model.SearchList
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : BaseViewModel(application) {


    private val _favouriteWifiList = MutableLiveData<List<SearchList>>(
        listOf(
            SearchList("Avator", "prashantj@gmail.com", "prashant", 1, "KK"),
            SearchList("Avator", "prashantj@gmail.com", "prashant", 2, "KK")
        )

    )
    val favouriteWifiList: LiveData<List<SearchList>>
        get() = _favouriteWifiList


    private val _markFavouriteResponse = MutableLiveData<BaseResponse<MarkFavouriteResponse>>()
    val markFavouriteResponse: LiveData<BaseResponse<MarkFavouriteResponse>>
        get() = _markFavouriteResponse

    private val _getFavouriteResponse = MutableLiveData<BaseResponse<List<GetFavouriteItem>>>()

    val getFavouriteResponse: LiveData<BaseResponse<List<GetFavouriteItem>>>
        get() = _getFavouriteResponse


    init {
        getFavouriteList()
    }

    fun markFavouriteItem(locationId: Int) {
        setDialogVisibility(true,null)
        coroutineScope.launch {
            DataProvider.markFavourite(
                request = MarkFavouriteRequest(locationId = locationId),
                success = {

                    if (it.status)
                        getFavouriteList()

                    showToastFromViewModel(it.message)
                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }
            )
        }
    }

    private fun getFavouriteList() {

        setDialogVisibility(true,null)

        coroutineScope.launch {
            DataProvider.getFavourite(
                success = {

                    _getFavouriteResponse.value = it

                    setDialogVisibility(false)

                }, error = {

                    checkError(it)
                }
            )
        }
    }


}