package com.mosamir.atmodrivecaptain.futures.auth.di

import com.mosamir.atmodrivecaptain.futures.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.CheckCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.ICheckCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.ISendCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.SendCodeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object UdeCaseModule {


    @Provides
    fun provideSendCodeUseCase(iAuthRepo: IAuthRepo):ISendCodeUseCase = SendCodeUseCase(iAuthRepo)

    @Provides
    fun provideCheckCodeUseCase(iAuthRepo: IAuthRepo):ICheckCodeUseCase = CheckCodeUseCase(iAuthRepo)


}