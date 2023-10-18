package com.mosamir.atmodrivecaptain.features.trip.domain.model

data class UpdateAvailabilityResponse(
    val message: String,
    val status: Boolean,
    val available:Boolean
)