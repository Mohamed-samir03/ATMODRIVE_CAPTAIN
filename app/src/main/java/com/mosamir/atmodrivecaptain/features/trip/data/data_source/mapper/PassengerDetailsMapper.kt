package com.mosamir.atmodrivecaptain.features.trip.data.data_source.mapper

import com.mosamir.atmodrivecaptain.features.trip.data.model.RemotePassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse

fun RemotePassengerDetailsResponse.asDomain()  = PassengerDetailsResponse(
    data?.asDomain(),
    message,
    status
)