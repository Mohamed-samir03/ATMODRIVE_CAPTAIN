package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IArrivedTripUseCase {

    suspend fun arrivedTrip(tripId: Int): IResult<TripStatusResponse>

}