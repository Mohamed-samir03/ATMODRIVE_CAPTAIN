package com.mosamir.atmodrivecaptain.features.trip.domain.model

data class TripStatusResponse(
    val message: String,
    val status: Boolean,
    val data: EndTripData?
)