package com.ey.hotspot.app_core_lib

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ey.hotspot.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    //create a local app instance
    protected val appInstance: Application by lazy { application }

    //Create a viewModel Job
    private val viewModelJob = Job()
    protected val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    /*  Toast message  */
    protected val _toastMessage = MutableLiveData<Event<String?>>()
    val toastMessage: LiveData<Event<String?>>
        get() = _toastMessage

    //used to store duration of toast message
    var toastMessageDuration: Boolean = false
        private set

    //    Dialog Message
    private val _dialogMessage = MutableLiveData<String>()
    val dialogMessage: LiveData<String>
        get() = _dialogMessage

    //    Dialog Visibility
    private val _dialogVisibility = MutableLiveData<Boolean>()
    val dialogVisibility: LiveData<Boolean>
        get() = _dialogVisibility

    /**
     * Method to check for error/exception
     *
     * @param e Takes an Exception as argument
     */
    protected fun checkError(e: Exception) {
        _dialogVisibility.postValue(false)
        e.message?.let {
            if (it.contains("500") || it.contains("Socket"))
                showToastFromViewModel("No Internet Connection $it", true)
            else
                showToastFromViewModel(it, true)
        }
    }

    /**
     * Method to show/hide dialog & set its message
     *
     * @param show Determine whether to show/hide the dialog
     * @param message The message to be shown in dialog
     */
    protected fun setDialogVisibility(show: Boolean, message: String? = null){
        _dialogVisibility.postValue(show)
        _dialogMessage.postValue(message)
    }

    /**
     * Method to show a toast from viewModel
     *
     * @param durationLong Determines duration of Toast
     * @param message The message to be shown in Toast
     */
    protected fun showToastFromViewModel(message: String?, durationLong: Boolean = false){
        _toastMessage.postValue(Event(message))
        toastMessageDuration = durationLong
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}