package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IOnTripUseCase {

    suspend fun onTrip(): IResult<PassengerDetailsResponse>

}