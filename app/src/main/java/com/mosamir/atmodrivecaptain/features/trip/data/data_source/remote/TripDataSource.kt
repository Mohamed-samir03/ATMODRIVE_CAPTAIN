package com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote

import com.mosamir.atmodrivecaptain.features.trip.data.data_source.mapper.asDomain
import com.mosamir.atmodrivecaptain.features.trip.data.model.RemoteTripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.data.model.RemoteUpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import java.lang.Exception
import javax.inject.Inject

class TripDataSource @Inject constructor(
    private val tripApiService: TripApiService
):ITripDataSource {


    override suspend fun updateAvailability(
        captainLat: String,
        captainLng: String
    ): IResult<UpdateAvailabilityResponse> {
        return try {
            val updateAvaData = tripApiService.updateAvailability(captainLat,captainLng)
            if (updateAvaData.status){
                return IResult.onSuccess(updateAvaData.asDomain())
            }else{
                return IResult.onFail(updateAvaData.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun getPassengerDetails(tripId: Int): IResult<PassengerDetailsResponse> {
        return try {
            val passengerData = tripApiService.getPassengerDetails(tripId)
            if (passengerData.status){
                return IResult.onSuccess(passengerData.asDomain())
            }else{
                return IResult.onFail(passengerData.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun acceptTrip(
        tripId: Int,
        captainLat: String,
        captainLng: String,
        captainLocName: String
    ): IResult<TripStatusResponse> {
        return try {
            val data = tripApiService.acceptTrip(tripId,captainLat,captainLng, captainLocName)
            if (data.status){
                return IResult.onSuccess(data.asDomain())
            }else{
                return IResult.onFail(data.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun pickUpTrip(tripId: Int): IResult<TripStatusResponse> {
        return try {
            val data = tripApiService.pickUpTrip(tripId)
            if (data.status){
                return IResult.onSuccess(data.asDomain())
            }else{
                return IResult.onFail(data.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun arrivedTrip(tripId: Int): IResult<TripStatusResponse> {
        return try {
            val data = tripApiService.arrivedTrip(tripId)
            if (data.status){
                return IResult.onSuccess(data.asDomain())
            }else{
                return IResult.onFail(data.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun startTrip(tripId: Int): IResult<TripStatusResponse> {
        return try {
            val data = tripApiService.startTrip(tripId)
            if (data.status){
                return IResult.onSuccess(data.asDomain())
            }else{
                return IResult.onFail(data.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun cancelTrip(tripId: Int): IResult<TripStatusResponse> {
        return try {
            val data = tripApiService.cancelTrip(tripId)
            if (data.status){
                return IResult.onSuccess(data.asDomain())
            }else{
                return IResult.onFail(data.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }


}