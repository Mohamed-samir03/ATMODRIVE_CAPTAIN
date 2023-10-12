package com.mosamir.atmodrivecaptain.features.trip.di

import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IUpdateAvailabilityUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.UpdateAvailabilityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object TripUdeCaseModule {


    @Provides
    fun provideUpdateAvailabilityUseCase(iTripRepo: ITripRepo):IUpdateAvailabilityUseCase
            = UpdateAvailabilityUseCase(iTripRepo)


}