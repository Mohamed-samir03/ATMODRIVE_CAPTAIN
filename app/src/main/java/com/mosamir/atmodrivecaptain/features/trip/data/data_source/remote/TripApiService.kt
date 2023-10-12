package com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote


import com.mosamir.atmodrivecaptain.features.trip.data.model.RemoteUpdateAvailabilityResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


const val UPDATE_AVAILABILITY = "update-availability"

interface TripApiService {

    @POST(UPDATE_AVAILABILITY)
    @FormUrlEncoded
    suspend fun updateAvailability(
        @Field("captain_lat") captainLat: String,
        @Field("captain_lng") captainLng: String
    ):RemoteUpdateAvailabilityResponse

}