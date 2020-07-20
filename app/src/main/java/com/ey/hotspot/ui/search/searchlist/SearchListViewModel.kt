package com.ey.hotspot.ui.search.searchlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import kotlinx.coroutines.launch

class SearchListViewModel(application: Application): BaseViewModel(application){

    //Skipped user
    private val _getHotSpotResponse = MutableLiveData<List<GetHotSpotResponse>>()
    val getHotSpotResponse: LiveData<List<GetHotSpotResponse>>
        get() = _getHotSpotResponse

    //Variable to store search string
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
                        _getHotSpotResponse.value = it.data
                    } else {
                        showToastFromViewModel(it.message)
                    }

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
                        getHotSpotResponse(searchString)

                    showToastFromViewModel(it.message)
                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }
            )
        }
    }

}