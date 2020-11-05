package com.ey.hotspot.ui.favourite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.GetFavoriteRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : BaseViewModel(application) {


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
                    else
                        setDialogVisibility(false)

                    showToastFromViewModel(it.message)
                }, error = {
                    checkError(it)
                }
            )
        }
    }

    /**
     * Method to get favourites list
     */
    fun getFavouriteList(value: String = "") {
        val request = GetFavoriteRequest(name = value)

        setDialogVisibility(true,null)

        coroutineScope.launch {
            DataProvider.getFavourite(
                request = request,
                success = {
                    setDialogVisibility(false)
                    _getFavouriteResponse.value = it



                }, error = {

                    checkError(it)
                }
            )
        }
    }


}