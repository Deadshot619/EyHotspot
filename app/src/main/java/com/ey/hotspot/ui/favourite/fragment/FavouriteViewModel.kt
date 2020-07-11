package com.ey.hotspot.ui.favourite.fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.ey.hotspot.network.response.BaseResponse
import com.ey.hotspot.ui.favourite.model.GetFavouriteItem
import com.ey.hotspot.ui.favourite.model.MarkFavouriteRequest
import com.ey.hotspot.ui.favourite.model.MarkFavouriteResponse
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : BaseViewModel(application) {


    private val _markFavouriteResponse = MutableLiveData<BaseResponse<MarkFavouriteResponse>>()

    val markFavouriteResponse: LiveData<BaseResponse<MarkFavouriteResponse>>
        get() = _markFavouriteResponse

    private val _getFavouriteResponse = MutableLiveData<BaseResponse<List<GetFavouriteItem>>>()

    val getFavouriteResponse: LiveData<BaseResponse<List<GetFavouriteItem>>>
        get() = _getFavouriteResponse

    fun markFavouriteItem(markFavouriteRequest: MarkFavouriteRequest) {

        coroutineScope.launch {
            DataProvider.markFavourite(
                request = markFavouriteRequest,
                success = {

                    _markFavouriteResponse.value = it
                    Log.d("MarkResponse", "" + it)
                }, error = {
                    _errorText.value = it.message
                    Log.d("MarkResponse", "" + it)
                }

            )
        }

    }

    fun getFavouriteList() {


        coroutineScope.launch {
            DataProvider.getFavourite(
                success = {

                    _getFavouriteResponse.value = it
                    Log.d("GetFavourite", "" + it)
                }, error = {
                    _errorText.value = it.message
                    Log.d("GetFavourite", "" + it)
                }
            )
        }
    }

}