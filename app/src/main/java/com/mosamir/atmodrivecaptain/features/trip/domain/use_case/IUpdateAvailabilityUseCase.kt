package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IUpdateAvailabilityUseCase {

    suspend fun updateAvailability(captainLat: String, captainLng: String): IResult<UpdateAvailabilityResponse>

}