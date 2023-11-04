package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface ICancelTripUseCase {

    suspend fun cancelTrip(tripId: Int): IResult<TripStatusResponse>

}