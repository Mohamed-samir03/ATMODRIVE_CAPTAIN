package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class AcceptTripUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
):IAcceptTripUseCase {
    override suspend fun acceptTrip(
        tripId: Int,
        captainLat: String,
        captainLng: String,
        captainLocName: String
    ): IResult<TripStatusResponse> {
        return iTripRepo.acceptTrip(tripId,captainLat,captainLng, captainLocName)
    }
}