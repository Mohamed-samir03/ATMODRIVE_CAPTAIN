package com.mosamir.atmodrivecaptain.features.trip.data.data_source.mapper

import com.mosamir.atmodrivecaptain.features.trip.data.model.RemoteEndTripData
import com.mosamir.atmodrivecaptain.features.trip.domain.model.EndTripData

fun RemoteEndTripData.asDomain() = EndTripData(
    cost, id
)