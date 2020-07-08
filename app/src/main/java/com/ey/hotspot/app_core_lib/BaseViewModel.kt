package com.ey.hotspot.app_core_lib

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val viewModelJob = Job()
    protected val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    protected val _errorText = MutableLiveData<String>()

    val errorText: LiveData<String>
        get() = _errorText


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}