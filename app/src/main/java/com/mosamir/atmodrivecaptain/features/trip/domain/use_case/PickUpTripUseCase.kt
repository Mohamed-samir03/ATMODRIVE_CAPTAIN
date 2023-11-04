package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class PickUpTripUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
): IPickUpTripUseCase {
    override suspend fun pickUpTrip(tripId: Int): IResult<TripStatusResponse> {
        return iTripRepo.pickUpTrip(tripId)
    }
}