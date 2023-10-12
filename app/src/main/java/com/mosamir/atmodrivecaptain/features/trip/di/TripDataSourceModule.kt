package com.mosamir.atmodrivecaptain.features.trip.di

import com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote.ITripDataSource
import com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote.TripApiService
import com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote.TripDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object TripDataSourceModule {



    @Provides
    fun getTripDataSource(tripApiService: TripApiService):ITripDataSource
            = TripDataSource(tripApiService)


}