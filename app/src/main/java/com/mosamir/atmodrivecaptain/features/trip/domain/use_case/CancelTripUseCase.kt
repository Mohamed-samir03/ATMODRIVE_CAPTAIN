package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class CancelTripUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
): ICancelTripUseCase {
    override suspend fun cancelTrip(tripId: Int): IResult<TripStatusResponse> {
        return iTripRepo.cancelTrip(tripId)
    }
}