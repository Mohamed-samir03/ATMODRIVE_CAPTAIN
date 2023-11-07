package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class EndTripUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
):IEndTripUseCase {
    override suspend fun endTrip(
        tripId: Int,
        dropOffLat: String,
        dropOffLng: String,
        dropOffLocName: String,
        distance: Double
    ): IResult<TripStatusResponse> {
        return iTripRepo.endTrip(tripId, dropOffLat, dropOffLng, dropOffLocName, distance)
    }
}