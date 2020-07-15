package com.ey.hotspot.ui.home.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.R
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.home.models.GetUserHotSpotResponse
import kotlinx.coroutines.launch

class HomeFragmentViewModel(application: Application) : BaseViewModel(application) {


    private val _getHotSpotResponse = MutableLiveData<BaseResponse<List<GetHotSpotResponse>>>()
    val getHotSpotResponse: LiveData<BaseResponse<List<GetHotSpotResponse>>>
        get() = _getHotSpotResponse


    private val _getUserHotSpotResponse =
        MutableLiveData<BaseResponse<List<GetUserHotSpotResponse>>>()
    val getUserHotSpotResponse: LiveData<BaseResponse<List<GetUserHotSpotResponse>>>
        get() = _getUserHotSpotResponse


    private val _markFavouriteResponse = MutableLiveData<BaseResponse<MarkFavouriteResponse>>()

    val markFavouriteResponse: LiveData<BaseResponse<MarkFavouriteResponse>>
        get() = _markFavouriteResponse


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


    fun getUserHotSpotResponse(getHotSpotRequest: GetHotSpotRequest) {

        coroutineScope.launch {
            DataProvider.getUserHotSpot(
                request = getHotSpotRequest,
                success = {
                    _getUserHotSpotResponse.value = it

                }, error = {

                    checkError(it)
                }
            )
        }
    }

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