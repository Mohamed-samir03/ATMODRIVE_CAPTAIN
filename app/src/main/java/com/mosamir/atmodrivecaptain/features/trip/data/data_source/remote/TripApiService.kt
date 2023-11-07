package com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote


import com.mosamir.atmodrivecaptain.features.trip.data.model.RemotePassengerDetailsResponse
import com.mosamir.atmodrivecaptain.features.trip.data.model.RemoteTripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.data.model.RemoteUpdateAvailabilityResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


const val UPDATE_AVAILABILITY = "update-availability"
const val GET_PASSENGER_DETAILS = "get-passenger-details-for-trip"
const val ACCEPT_TRIP = "accept-trip"
const val PICKUP_TRIP = "pickup-trip"
const val ARRIVED_TRIP = "arrived-trip"
const val START_TRIP = "start-trip"
const val CANCEL_TRIP = "cancel-trip"
const val END_TRIP = "end-trip"

interface TripApiService {

    @POST(UPDATE_AVAILABILITY)
    @FormUrlEncoded
    suspend fun updateAvailability(
        @Field("captain_lat") captainLat: String,
        @Field("captain_lng") captainLng: String
    ):RemoteUpdateAvailabilityResponse

    @POST(GET_PASSENGER_DETAILS)
    @FormUrlEncoded
    suspend fun getPassengerDetails(
        @Field("trip_id") tripId: Int
    ):RemotePassengerDetailsResponse

    @POST(ACCEPT_TRIP)
    @FormUrlEncoded
    suspend fun acceptTrip(
        @Field("trip_id") tripId: Int,
        @Field("captain_lat") captainLat: String,
        @Field("captain_lng") captainLng: String,
        @Field("captain_location_name") captainLocName: String
    ):RemoteTripStatusResponse

    @POST(PICKUP_TRIP)
    @FormUrlEncoded
    suspend fun pickUpTrip(
        @Field("trip_id") tripId: Int
    ):RemoteTripStatusResponse

    @POST(ARRIVED_TRIP)
    @FormUrlEncoded
    suspend fun arrivedTrip(
        @Field("trip_id") tripId: Int
    ):RemoteTripStatusResponse

    @POST(START_TRIP)
    @FormUrlEncoded
    suspend fun startTrip(
        @Field("trip_id") tripId: Int
    ):RemoteTripStatusResponse

    @POST(CANCEL_TRIP)
    @FormUrlEncoded
    suspend fun cancelTrip(
        @Field("trip_id") tripId: Int
    ):RemoteTripStatusResponse

    @POST(END_TRIP)
    @FormUrlEncoded
    suspend fun endTrip(
        @Field("trip_id") tripId: Int,
        @Field("dropoff_lat") dropOffLat: String,
        @Field("dropoff_lng") dropOffLng: String,
        @Field("dropoff_location_name") dropOffLocName: String,
        @Field("distance") distance: Double,
    ):RemoteTripStatusResponse

}