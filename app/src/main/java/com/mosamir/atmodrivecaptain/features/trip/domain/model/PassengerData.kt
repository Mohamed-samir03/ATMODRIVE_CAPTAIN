package com.mosamir.atmodrivecaptain.features.trip.domain.model

data class PassengerData(
    val avatar: String,
    val full_name: String,
    val id: Int,
    val mobile: String,
    val passenger_code: String,
    val rate: Double
)