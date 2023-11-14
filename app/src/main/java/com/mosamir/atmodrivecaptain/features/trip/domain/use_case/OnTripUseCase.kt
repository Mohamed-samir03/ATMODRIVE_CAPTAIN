package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class OnTripUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
) :IOnTripUseCase {
    override suspend fun onTrip(): IResult<PassengerDetailsResponse> {
        return iTripRepo.onTrip()
    }
}