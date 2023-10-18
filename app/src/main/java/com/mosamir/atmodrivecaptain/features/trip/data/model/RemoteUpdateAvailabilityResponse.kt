package com.mosamir.atmodrivecaptain.features.trip.data.model

data class RemoteUpdateAvailabilityResponse(
    val message: String,
    val status: Boolean,
    val available:Boolean
)