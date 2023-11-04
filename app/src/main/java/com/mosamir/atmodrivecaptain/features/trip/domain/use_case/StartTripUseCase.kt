package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class StartTripUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
):IStartTripUseCase {
    override suspend fun startTrip(tripId: Int): IResult<TripStatusResponse> {
        return iTripRepo.startTrip(tripId)
    }
}