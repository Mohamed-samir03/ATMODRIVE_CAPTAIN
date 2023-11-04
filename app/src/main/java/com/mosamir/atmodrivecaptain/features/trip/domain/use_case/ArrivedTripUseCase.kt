package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class ArrivedTripUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
):IArrivedTripUseCase {
    override suspend fun arrivedTrip(tripId: Int): IResult<TripStatusResponse> {
        return iTripRepo.arrivedTrip(tripId)
    }
}