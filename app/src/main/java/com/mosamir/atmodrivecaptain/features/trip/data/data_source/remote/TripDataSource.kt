package com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote

import com.mosamir.atmodrivecaptain.features.trip.data.data_source.mapper.asDomain
import com.mosamir.atmodrivecaptain.features.trip.data.model.RemoteUpdateAvailabilityResponse
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


}