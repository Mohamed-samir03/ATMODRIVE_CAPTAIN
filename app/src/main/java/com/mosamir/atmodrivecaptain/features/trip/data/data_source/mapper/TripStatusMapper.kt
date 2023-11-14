package com.mosamir.atmodrivecaptain.features.trip.data.data_source.mapper

import com.mosamir.atmodrivecaptain.features.trip.data.model.RemoteTripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse

fun RemoteTripStatusResponse.asDomain() = TripStatusResponse(
    message, status ,data?.asDomain()
)