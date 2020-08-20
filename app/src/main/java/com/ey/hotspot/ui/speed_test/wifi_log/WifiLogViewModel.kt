package com.ey.hotspot.ui.speed_test.wifi_log

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.speed_test.wifi_log_list.WifiLogListFragment
import kotlinx.coroutines.launch

class WifiLogViewModel(application: Application) : BaseViewModel(application) {

    private val _favouriteStatus = MutableLiveData<Boolean>()
    val favouriteStatus: LiveData<Boolean>
    get() = _favouriteStatus


    fun markFavouriteItem(locationId: Int, isFavourite: Boolean? = false) {
        setDialogVisibility(true,null)
        coroutineScope.launch {
            DataProvider.markFavourite(
                request = MarkFavouriteRequest(locationId = locationId),
                success = {

                    if (it.status){
                        showToastFromViewModel(it.message)
                        WifiLogListFragment.RELOAD = true
                        _favouriteStatus.value = !(isFavourite ?: true)
                    }

                    setDialogVisibility(false)
                }, error = {
                    checkError(it)
                }
            )
        }
    }

}