package com.mosamir.atmodrivecaptain.features.trip.domain.model

data class PassengerDetailsResponse(
    val `data`: PassengerDetailsData?,
    val message: String,
    val status: Boolean
)