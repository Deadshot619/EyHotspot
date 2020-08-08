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
import kotlinx.coroutines.launch

class SearchListViewModel(application: Application) : BaseViewModel(application) {

    //Skipped user
    private val _getHotSpotResponse = MutableLiveData<BaseResponse<List<GetHotSpotResponse>>>()
    val getHotSpotResponse: LiveData<BaseResponse<List<GetHotSpotResponse>>>
        get() = _getHotSpotResponse

    //Variable to store search string
    private var searchString = ""

    init {
        getHotSpotResponse("")
    }

    //Method to get hotspots list for non-logged in user
    fun getHotSpotResponse(value: String) {
        val request = GetHotSpotRequest(
            name = value,
            latitude = 0.0,
            longitude = 0.0,
            locationEnabled = false
        )

        searchString = value
        setDialogVisibility(true)
        coroutineScope.launch {
            DataProvider.getHotspot(
                request = request,
                success = {

                    if (it.status) {
                        _getHotSpotResponse.postValue(it)
                    } else {
                        showToastFromViewModel(it.message)
                    }
                    setDialogVisibility(false)


                }, error = {
                    checkError(it)
                }
            )
        }
    }


    fun markFavouriteItem(locationId: Int) {
        setDialogVisibility(true, null)
        coroutineScope.launch {
            DataProvider.markFavourite(
                request = MarkFavouriteRequest(locationId = locationId),
                success = {

                    if (it.status)
                        getHotSpotResponse(searchString)
                    else
                        setDialogVisibility(false)

                    showToastFromViewModel(it.message)
                }, error = {
                    checkError(it)
                }
            )
        }
    }

}