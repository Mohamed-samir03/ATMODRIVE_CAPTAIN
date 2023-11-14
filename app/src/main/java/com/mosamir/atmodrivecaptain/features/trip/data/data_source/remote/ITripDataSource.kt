package com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote

import com.mosamir.atmodrivecaptain.features.trip.domain.model.PassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.model.UpdateAvailabilityResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface ITripDataSource {

    suspend fun updateAvailability(captainLat: String, captainLng: String): IResult<UpdateAvailabilityResponse>
    suspend fun getPassengerDetails(tripId: Int): IResult<PassengerDetailsResponse>

    suspend fun acceptTrip(
        tripId: Int,captainLat: String,captainLng: String,captainLocName: String
    ): IResult<TripStatusResponse>

    suspend fun pickUpTrip(tripId: Int): IResult<TripStatusResponse>

    suspend fun arrivedTrip(tripId: Int): IResult<TripStatusResponse>

    suspend fun startTrip(tripId: Int): IResult<TripStatusResponse>

    suspend fun cancelTrip(tripId: Int): IResult<TripStatusResponse>

    suspend fun endTrip(tripId: Int,dropOffLat: String,dropOffLng: String,dropOffLocName: String,distance: Double,
    ):IResult<TripStatusResponse>

    suspend fun onTrip(): IResult<PassengerDetailsResponse>

}