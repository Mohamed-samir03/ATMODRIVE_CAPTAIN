package com.mosamir.atmodrivecaptain.features.trip.di

import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.AcceptTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.ArrivedTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.CancelTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.EndTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.GetPassengerDetailsUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IAcceptTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IArrivedTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.ICancelTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IEndTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IGetPassengerDetailsUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IOnTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IPickUpTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IStartTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.IUpdateAvailabilityUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.OnTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.PickUpTripUseCase
import com.mosamir.atmodrivecaptain.features.trip.domain.use_case.StartTripUseCase
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

    @Provides
    fun providePickUpTripUseCase(iTripRepo: ITripRepo):IPickUpTripUseCase
            = PickUpTripUseCase(iTripRepo)

    @Provides
    fun provideArrivedTripUseCase(iTripRepo: ITripRepo):IArrivedTripUseCase
            = ArrivedTripUseCase(iTripRepo)

    @Provides
    fun provideStartTripUseCase(iTripRepo: ITripRepo):IStartTripUseCase
            = StartTripUseCase(iTripRepo)

    @Provides
    fun provideCancelTripUseCase(iTripRepo: ITripRepo):ICancelTripUseCase
            = CancelTripUseCase(iTripRepo)

    @Provides
    fun provideEndTripUseCase(iTripRepo: ITripRepo):IEndTripUseCase
            = EndTripUseCase(iTripRepo)

    @Provides
    fun provideOnTripUseCase(iTripRepo: ITripRepo):IOnTripUseCase
            = OnTripUseCase(iTripRepo)


}