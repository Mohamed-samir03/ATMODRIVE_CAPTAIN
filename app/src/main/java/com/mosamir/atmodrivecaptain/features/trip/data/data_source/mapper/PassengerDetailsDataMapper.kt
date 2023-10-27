package com.mosamir.atmodrivecaptain.features.trip.data.data_source.mapper

import com.mosamir.atmodrivecaptain.features.trip.data.model.RemotePassengerDetailsData
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsData

fun RemotePassengerDetailsData.asDomain() = PassengerDetailsData(
    cost,
    created_at,
    dropoff_lat,
    dropoff_lng,
    dropoff_location_name,
    estimate_time,
    id,
    trip_code,
    trip_color,
    trip_status
)