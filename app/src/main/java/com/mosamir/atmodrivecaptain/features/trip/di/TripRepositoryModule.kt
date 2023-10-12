package com.mosamir.atmodrivecaptain.features.trip.di

import com.mosamir.atmodrivecaptain.features.trip.data.data_source.remote.ITripDataSource
import com.mosamir.atmodrivecaptain.features.trip.data.repository.TripRepo
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object TripRepositoryModule {


    @Provides
    fun provideTripRepo(iTripDataSource: ITripDataSource):ITripRepo
            = TripRepo(iTripDataSource)


}