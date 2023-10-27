package com.mosamir.atmodrivecaptain.features.trip.presentation.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    val requestStatus = MutableLiveData<Boolean>()
    val tripId = MutableLiveData<Int>()


    fun setRequestStatus(status: Boolean) {
        requestStatus.value = status
    }

    fun setTripId(id: Int) {
        tripId.value = id
    }

}