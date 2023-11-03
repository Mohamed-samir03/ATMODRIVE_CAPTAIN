package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IAcceptTripUseCase {

    suspend fun acceptTrip(
        tripId: Int,captainLat: String,captainLng: String,captainLocName: String
    ): IResult<TripStatusResponse>

}