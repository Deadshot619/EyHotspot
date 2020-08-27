package com.ey.hotspot.ui.home.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.database.wifi_info.WifiInfoDatabase
import com.ey.hotspot.database.wifi_info.WifiInfoDatabaseDao
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.request.WifiLogoutRequest
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import com.ey.hotspot.ui.home.models.*
import com.ey.hotspot.utils.Event
import com.ey.hotspot.utils.constants.getDeviceId
import com.ey.hotspot.utils.extention_functions.parseToDouble
import com.ey.hotspot.utils.extention_functions.toServerFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeFragmentViewModel(application: Application) : BaseViewModel(application) {

    private lateinit var database: WifiInfoDatabaseDao
    private val DEVICE_ID = getDeviceId()


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

    var deepLinkedUuid: String? = ""
    val goToDeepLinkedLocation = MutableLiveData<Event<Boolean>>(Event(true))
    val saveLinkedLocationIfExists = MutableLiveData<MyClusterItems>()


    init {
        //get instance of database
        database = WifiInfoDatabase.getInstance(appInstance).wifiInfoDatabaseDao

        getHotSpotResponse()

        coroutineScope.launch {
            getLastInsertedData()
        }
    }

    //Get HotSpots
    private fun getHotSpotResponse() {
        val request = GetHotSpotRequest(name = "")

        setDialogVisibility(true)
        coroutineScope.launch {
            DataProvider.getHotspot(
                request = request,
                success = {

                    if (it.status){
                        setDialogVisibility(false)
                        listClusterItem.clear()
                        for (i in it.data){
                            if (i.uuid == deepLinkedUuid){
                                saveLinkedLocationIfExists.value = MyClusterItems(
                                    lat = i.lat.parseToDouble(),
                                    lng = i.lng.parseToDouble(),
                                    navigateURL = i.navigate_url,
                                    title = i.name,
                                    snippet = i.provider_name,
                                    isfavourite = i.favourite,
                                    itemId = i.id,
                                    address = i.location,
                                    rating = i.average_rating,
                                    uuid = i.uuid
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
                                    address = i.location,
                                    rating = i.average_rating,
                                    uuid = i.uuid
                                )
                            )
                        }
                    }

                    _getHotSpotResponse.value = Event(it)



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





    private suspend fun getLastInsertedData(){
        withContext(Dispatchers.IO){
            //Get wifi login data from db
            val data = database.getLastInsertedData()

            /*
             *  Check whether data is present, if it is then check whether it is synced, If yes then
             */
            if (data.isNotEmpty() && !data[0].synced && data[0].disconnectedOn != null)
                callWifiLogout(data[0].id, data[0].wifiId, DEVICE_ID, Calendar.getInstance().time)

//            updateLogoutTimeInDb(data)
        }
    }


    /*
     *  Method to call WifiLogout Api.
     *  This api will be called when a wifi connection will be available
     */
    private suspend fun callWifiLogout(dbId: Long, wifiId: Int, deviceId: String, logoutAt: Date) {
        val request = WifiLogoutRequest(
            wifi_id = wifiId,
            device_id = deviceId,
            logout_at = logoutAt.toServerFormat()
        )


        DataProvider.wifiLogout(
            request = request,
            success = {
                if (it.status) {
                    //If Wifi logout is successful, then update sync status of the data in DB
                    coroutineScope.launch {
                        updateSyncStatusOfDataInDb(dbId = dbId, syncStatus = it.status)
                    }
                }
            },
            error = {
            }
        )
    }

    /*
     *  This method will update current sync status of Data in DB.
     *  Will be called when WiFi logout api has been called & wifi has successfully logged out
     */
    private suspend fun updateSyncStatusOfDataInDb(dbId: Long, syncStatus: Boolean) {
        withContext(Dispatchers.IO) {
            database.updateSyncStatus(id = dbId, sync = syncStatus)
        }
    }


}