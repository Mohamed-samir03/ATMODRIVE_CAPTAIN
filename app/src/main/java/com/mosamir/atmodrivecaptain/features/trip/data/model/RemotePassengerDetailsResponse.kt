package com.mosamir.atmodrivecaptain.features.trip.data.model

data class RemotePassengerDetailsResponse(
    val `data`: RemotePassengerDetailsData?,
    val message: String,
    val status: Boolean
)