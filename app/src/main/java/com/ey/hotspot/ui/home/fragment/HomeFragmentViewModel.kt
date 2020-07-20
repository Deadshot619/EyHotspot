package com.ey.hotspot.ui.home.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import kotlinx.coroutines.launch

class HomeFragmentViewModel(application: Application) : BaseViewModel(application) {

    //Variable to store response of HotSpot
    private val _getHotSpotResponse = MutableLiveData<BaseResponse<List<GetHotSpotResponse>>>()
    val getHotSpotResponse: LiveData<BaseResponse<List<GetHotSpotResponse>>>
        get() = _getHotSpotResponse

    //Variable to store response of Favourites
    private val _markFavouriteResponse = MutableLiveData<BaseResponse<MarkFavouriteResponse>>()
    val markFavouriteResponse: LiveData<BaseResponse<MarkFavouriteResponse>>
        get() = _markFavouriteResponse

    //Get HotSpots
    fun getHotSpotResponse(getHotSpotRequest: GetHotSpotRequest) {

        coroutineScope.launch {
            DataProvider.getHotspot(
                request = getHotSpotRequest,
                success = {

                    _getHotSpotResponse.value = it
                }, error = {

                    checkError(it)

                }
            )
        }
    }

    //Mark a hotspot as favourite
    fun markFavouriteItem(markFavouriteRequest: MarkFavouriteRequest,favouriteType:Boolean) {
        if(favouriteType) {
            setDialogVisibility(true,null)
        }else{
            setDialogVisibility(true, null)

        }

        coroutineScope.launch {
            DataProvider.markFavourite(
                request = markFavouriteRequest,
                success = {
                    _markFavouriteResponse.value = it

                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }

            )
        }
    }

}