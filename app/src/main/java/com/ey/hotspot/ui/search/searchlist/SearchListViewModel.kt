package com.ey.hotspot.ui.search.searchlist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.home.models.GetHotSpotRequest
import com.ey.hotspot.ui.home.models.GetHotSpotResponse
import com.ey.hotspot.ui.home.models.GetUserHotSpotResponse
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


    //Method to get hotspots list for non-logged in user
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

    //Method to get hotspots list for logged in user
    fun getUserHotSpotResponse(value: String) {
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


}