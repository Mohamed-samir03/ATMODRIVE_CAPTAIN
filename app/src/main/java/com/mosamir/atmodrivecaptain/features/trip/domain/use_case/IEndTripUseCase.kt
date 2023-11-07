package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IEndTripUseCase {

    suspend fun endTrip(tripId: Int,dropOffLat: String,dropOffLng: String,dropOffLocName: String,distance: Double,
    ): IResult<TripStatusResponse>

}