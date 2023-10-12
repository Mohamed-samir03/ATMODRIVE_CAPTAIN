package com.mosamir.atmodrivecaptain.features.trip.domain.repository

import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface ITripRepo {

    suspend fun updateAvailability(captainLat: String, captainLng: String): IResult<UpdateAvailabilityResponse>

}