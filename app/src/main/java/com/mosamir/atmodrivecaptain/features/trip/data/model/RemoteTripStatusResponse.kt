package com.mosamir.atmodrivecaptain.features.trip.data.model

data class RemoteTripStatusResponse(
    val message: String,
    val status: Boolean,
    val data:RemoteEndTripData?
)