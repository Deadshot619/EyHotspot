package com.ey.hotspot.ui.speed_test.wifi_log

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

class WifiLogViewModel(application: Application) : BaseViewModel(application) {
    private val _getFavouriteResponse = MutableLiveData<BaseResponse<List<GetFavouriteItem>>>()
    val getFavouriteResponse: LiveData<BaseResponse<List<GetFavouriteItem>>>
        get() = _getFavouriteResponse




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

                    _getFavouriteResponse.value = it

                    setDialogVisibility(false)

                }, error = {

                    checkError(it)
                }
            )
        }
    }
}