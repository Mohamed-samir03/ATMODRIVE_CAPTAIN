package com.mosamir.atmodrivecaptain.features.trip.data.data_source.mapper

import com.mosamir.atmodrivecaptain.features.trip.data.model.RemotePassengerData
import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerData

fun RemotePassengerData.asDomain() = PassengerData(
    avatar, full_name, id, mobile, passenger_code, rate
)