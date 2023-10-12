package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class UpdateAvailabilityUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
):IUpdateAvailabilityUseCase {
    override suspend fun updateAvailability(
        captainLat: String,
        captainLng: String
    ): IResult<UpdateAvailabilityResponse> {
        return iTripRepo.updateAvailability(captainLat,captainLng)
    }
}