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
import com.ey.hotspot.ui.home.models.MyClusterItems
import com.ey.hotspot.utils.Event
import kotlinx.coroutines.launch

class HomeFragmentViewModel(application: Application) : BaseViewModel(application) {

    //Variable to store response of HotSpot
    private val _getHotSpotResponse =
        MutableLiveData<Event<BaseResponse<List<GetHotSpotResponse>>>>()
    val getHotSpotResponse: LiveData<Event<BaseResponse<List<GetHotSpotResponse>>>>
        get() = _getHotSpotResponse

    //Variable to store response of Favourites
    private val _markFavouriteResponse = MutableLiveData<BaseResponse<MarkFavouriteResponse>>()
    val markFavouriteResponse: LiveData<BaseResponse<MarkFavouriteResponse>>
        get() = _markFavouriteResponse

    //Variable to store response of Favourites
    private val _markFavourite = MutableLiveData<Event<MyClusterItems?>>()
    val markFavourite: LiveData<Event<MyClusterItems?>>
        get() = _markFavourite


    //Get HotSpots
    fun getHotSpotResponse(getHotSpotRequest: GetHotSpotRequest) {
//        setDialogVisibility(true)
        coroutineScope.launch {
            DataProvider.getHotspot(
                request = getHotSpotRequest,
                success = {

                    _getHotSpotResponse.value = Event(it)
                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                    setDialogVisibility(false)
                }
            )
        }
    }

    //Mark a hotspot as favourite
    fun markFavouriteItem(
        markFavouriteRequest: MarkFavouriteRequest,
        favouriteType: Boolean,
        myClusterItems: MyClusterItems?
    ) {
        if (favouriteType) {
            setDialogVisibility(true, null)
        } else {
            setDialogVisibility(true, null)

        }

        coroutineScope.launch {
            DataProvider.markFavourite(
                request = markFavouriteRequest,
                success = {
//                    _markFavouriteResponse.value = it
                    if (it.status)
                        _markFavourite.value = Event(myClusterItems)

                    showToastFromViewModel(it.message)

                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                    setDialogVisibility(false)
                }

            )
        }
    }

}