package com.mosamir.atmodrivecaptain.features.trip.data.repository

import com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote.ITripDataSource
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class TripRepo @Inject constructor(
    private val iTripDataSource: ITripDataSource
):ITripRepo {

    override suspend fun updateAvailability(
        captainLat: String,
        captainLng: String
    ): IResult<UpdateAvailabilityResponse> {
        return iTripDataSource.updateAvailability(captainLat,captainLng)
    }

    override suspend fun getPassengerDetails(tripId: Int): IResult<PassengerDetailsResponse> {
        return iTripDataSource.getPassengerDetails(tripId)
    }

    override suspend fun acceptTrip(
        tripId: Int,
        captainLat: String,
        captainLng: String,
        captainLocName: String
    ): IResult<TripStatusResponse> {
        return iTripDataSource.acceptTrip(tripId,captainLat,captainLng, captainLocName)
    }


}