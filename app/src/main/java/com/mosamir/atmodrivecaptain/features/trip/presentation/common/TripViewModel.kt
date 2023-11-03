package com.mosamir.atmodrivecaptain.features.trip.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IAcceptTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IGetPassengerDetailsUseCase
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
    private val iUpdateAvailabilityUseCase: IUpdateAvailabilityUseCase,
    private val iGetPassengerDetailsUseCase: IGetPassengerDetailsUseCase,
    private val iAcceptTripUseCase: IAcceptTripUseCase
):ViewModel(){

    private val _updateAvaResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val updateAvaResult: StateFlow<NetworkState?> =_updateAvaResult

    private val _passengerDetails: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val passengerDetails: StateFlow<NetworkState?> =_passengerDetails

    private val _acceptTrip: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val acceptTrip: StateFlow<NetworkState?> =_acceptTrip


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

    fun getPassengerDetails(tripId:Int) {
        _passengerDetails.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iGetPassengerDetailsUseCase.getPassengerDetails(tripId)
                if (result.isSuccessful()){
                    _passengerDetails.value = NetworkState.getLoaded(result)
                }else{
                    _passengerDetails.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _passengerDetails.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

    fun acceptTrip(tripId: Int,captainLat: String,captainLng: String,captainLocName: String) {
        _acceptTrip.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iAcceptTripUseCase.acceptTrip(tripId,captainLat, captainLng, captainLocName)
                if (result.isSuccessful()){
                    _acceptTrip.value = NetworkState.getLoaded(result)
                }else{
                    _acceptTrip.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _acceptTrip.value = NetworkState.getErrorMessage(ex)
            }
        }
    }

}