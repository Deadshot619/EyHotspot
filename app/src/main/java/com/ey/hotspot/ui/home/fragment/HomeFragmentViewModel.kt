package com.ey.hotspot.ui.home.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.app_core_lib.BaseViewModel
import com.ey.hotspot.network.DataProvider
import com.google.gson.JsonArray
import kotlinx.coroutines.launch

class HomeFragmentViewModel(application: Application): BaseViewModel(application)  {



    private val _nearByWifiList = MutableLiveData<JsonArray>()
    val wifiList: LiveData<JsonArray>
        get() = _nearByWifiList


    //Call this method from fragment/layout
    fun getNearByWifiList(email: String, password: String){
        //request model, if exists
/*        val loginRequestModel = LoginRequestModel(email, password)*/

        coroutineScope.launch {
            DataProvider.getUserList(
                /*request = loginRequestModel,*/
                success = {
                    _nearByWifiList.value = it
                },
                error = {
                    //Whatever msg we have to display to user
                }
            )
        }
    }
}