package com.mosamir.atmodrivecaptain.features.trip.data.model

data class RemotePassengerDetailsData(
    val cost: String,
    val created_at: String,
    val dropoff_lat: String,
    val dropoff_lng: String,
    val dropoff_location_name: String,
    val pickup_lat: String,
    val pickup_lng: String,
    val pickup_location_name: String,
    val passenger:RemotePassengerData,
    val estimate_time: String,
    val id: Int,
    val trip_code: String,
    val trip_color: String,
    val trip_status: String
)