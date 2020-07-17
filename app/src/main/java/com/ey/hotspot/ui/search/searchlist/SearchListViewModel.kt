package com.ey.hotspot.ui.search.searchlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.home.models.GetUserHotSpotResponse
import com.ey.hotspot.utils.fromJson
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SearchListViewModel(application: Application): BaseViewModel(application){

    //Skipped user
    private val _getHotSpotResponse = MutableLiveData<BaseResponse<List<GetHotSpotResponse>>>()
    val getHotSpotResponse: LiveData<BaseResponse<List<GetHotSpotResponse>>>
        get() = _getHotSpotResponse

    //Signed in user
    private val _getUserHotSpotResponse = MutableLiveData<BaseResponse<List<GetUserHotSpotResponse>>>()
    val getUserHotSpotResponse: LiveData<BaseResponse<List<GetUserHotSpotResponse>>>
        get() = _getUserHotSpotResponse

    private var searchString = ""

    //Method to get hotspots list for non-logged in user
    fun getHotSpotResponse(value: String) {
        val request = GetHotSpotRequest(
            name = value,
            latitude = 19.1403509,
            longitude = 72.8096671,
            locationEnabled = true
        )

        coroutineScope.launch {
            DataProvider.getHotspot(
                request = request,
                success = {

                    if (it.status){
                        val str = Gson().toJson(it.data)

                        _getUserHotSpotResponse.value = BaseResponse(
                            status = it.status, message = it.message, data = Gson().fromJson<List<GetUserHotSpotResponse>>(str)!!
                        )
                    }

                }, error = {

                    checkError(it)
                }
            )
        }
    }

    //Method to get hotspots list for logged in user
    fun getUserHotSpotResponse(value: String) {
        searchString = value

        val request = GetHotSpotRequest(
            name = value,
            latitude = 19.1403509,
            longitude = 72.8096671,
            locationEnabled = true
        )


        coroutineScope.launch {
            DataProvider.getUserHotSpot(
                request = request,
                success = {
                    _getUserHotSpotResponse.value = it
                }, error = {

                    checkError(it)
                }
            )
        }
    }


    fun markFavouriteItem(locationId: Int) {
        setDialogVisibility(true,null)
        coroutineScope.launch {
            DataProvider.markFavourite(
                request = MarkFavouriteRequest(locationId = locationId),
                success = {

                    if (it.status)
                        getUserHotSpotResponse(searchString)

                    showToastFromViewModel(it.message)
                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }
            )
        }
    }

}