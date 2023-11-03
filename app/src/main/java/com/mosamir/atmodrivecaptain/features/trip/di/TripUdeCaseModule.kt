package com.mosamir.atmodrivecaptain.features.trip.di

import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.AcceptTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.GetPassengerDetailsUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IAcceptTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IGetPassengerDetailsUseCase
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

    @Provides
    fun providePassengerDetailsUseCase(iTripRepo: ITripRepo):IGetPassengerDetailsUseCase
            = GetPassengerDetailsUseCase(iTripRepo)

    @Provides
    fun provideAcceptTripUseCase(iTripRepo: ITripRepo):IAcceptTripUseCase
            = AcceptTripUseCase(iTripRepo)


}