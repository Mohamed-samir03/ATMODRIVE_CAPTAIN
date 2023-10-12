package com.mosamir.atmodrivecaptain.features.trip.data.data_source.mapper

import com.mosamir.atmodrivecaptain.features.trip.data.model.RemoteUpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse


fun RemoteUpdateAvailabilityResponse.asDomain()= UpdateAvailabilityResponse(
    message,
    status
)