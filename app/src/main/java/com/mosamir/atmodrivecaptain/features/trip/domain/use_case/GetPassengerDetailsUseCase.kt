package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class GetPassengerDetailsUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
):IGetPassengerDetailsUseCase {
    override suspend fun getPassengerDetails(tripId: Int): IResult<PassengerDetailsResponse> {
        return iTripRepo.getPassengerDetails(tripId)
    }
}