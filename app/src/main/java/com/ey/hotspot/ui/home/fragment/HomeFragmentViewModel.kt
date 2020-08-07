package com.ey.hotspot.ui.home.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import com.ey.hotspot.ui.home.models.*
import com.ey.hotspot.utils.Event
import com.ey.hotspot.utils.extention_functions.parseToDouble
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

    //Variable to hold cluster items
    var listClusterItem = mutableListOf<MyClusterItems>()
        private set

    var deepLinkedWifiId: Int = -1  //this variable will store id of wifi obtained from deeplink
    val goToDeepLinkedLocation = MutableLiveData<Event<Boolean>>(Event(true))
    val saveLinkedLocationIfExists = MutableLiveData<MyClusterItems>()


    init {
        getHotSpotResponse()
    }

    //Get HotSpots
    fun getHotSpotResponse() {
        val request = GetHotSpotRequest(name = "")

        setDialogVisibility(true)
        coroutineScope.launch {
            DataProvider.getHotspot(
                request = request,
                success = {

                    if (it.status){
                        listClusterItem.clear()
                        for (i in it.data){
                            if (i.id == deepLinkedWifiId){
                                saveLinkedLocationIfExists.value = MyClusterItems(
                                    lat = i.lat.parseToDouble(),
                                    lng = i.lng.parseToDouble(),
                                    navigateURL = i.navigate_url,
                                    title = i.name,
                                    snippet = i.provider_name,
                                    isfavourite = i.favourite,
                                    itemId = i.id,
                                    address = i.location
                                )
                            }
                            listClusterItem.add(
                                MyClusterItems(
                                    lat = i.lat.parseToDouble(),
                                    lng = i.lng.parseToDouble(),
                                    navigateURL = i.navigate_url,
                                    title = i.name,
                                    snippet = i.provider_name,
                                    isfavourite = i.favourite,
                                    itemId = i.id,
                                    address = i.location
                                )
                            )
                        }
                    }

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
        data: WifiInfoModel
    ) {
        setDialogVisibility(true, null)

        coroutineScope.launch {
            DataProvider.markFavourite(
                request = markFavouriteRequest,
                success = {
                    if (it.status)
                        _markFavourite.value = Event(data.toMyClusterItems())

                    showToastFromViewModel(it.message)

                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }

            )
        }
    }

}