package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IStartTripUseCase {

    suspend fun startTrip(tripId: Int): IResult<TripStatusResponse>

}