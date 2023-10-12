package com.mosamir.atmodrivecaptain.features.trip.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IUpdateAvailabilityUseCase
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.getError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TripViewModel @Inject constructor(
    private val iUpdateAvailabilityUseCase: IUpdateAvailabilityUseCase
):ViewModel(){

    private val _updateAvaResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val updateAvaResult: StateFlow<NetworkState?> =_updateAvaResult


    fun updateAvailability(captainLat: String,captainLng:String) {
        _updateAvaResult.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iUpdateAvailabilityUseCase.updateAvailability(captainLat, captainLng)
                if (result.isSuccessful()){
                    _updateAvaResult.value = NetworkState.getLoaded(result)
                }else{
                    _updateAvaResult.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _updateAvaResult.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

}