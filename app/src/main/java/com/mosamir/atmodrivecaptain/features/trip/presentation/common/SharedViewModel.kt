package com.mosamir.atmodrivecaptain.features.trip.presentation.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    val requestStatus = MutableLiveData<Boolean>()


    fun setRequestStatus(status: Boolean) {
        requestStatus.value = status
    }

}