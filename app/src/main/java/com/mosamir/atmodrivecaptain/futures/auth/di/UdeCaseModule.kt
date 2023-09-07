package com.mosamir.atmodrivecaptain.futures.auth.di

import com.mosamir.atmodrivecaptain.futures.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.CheckCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.ICheckCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.IRegisterBankAccountUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.IRegisterCaptainUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.IRegisterVehicleUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.ISendCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.IUploadFileUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.RegisterBankAccountUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.RegisterCaptainUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.RegisterVehicleUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.SendCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.UploadFileUseCase
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

    @Provides
    fun provideRegisterCaptainUseCase(iAuthRepo: IAuthRepo):IRegisterCaptainUseCase = RegisterCaptainUseCase(iAuthRepo)


    @Provides
    fun provideRegisterVehicleUseCase(iAuthRepo: IAuthRepo):IRegisterVehicleUseCase = RegisterVehicleUseCase(iAuthRepo)

    @Provides
    fun provideRegisterBankAccountUseCase(iAuthRepo: IAuthRepo):IRegisterBankAccountUseCase = RegisterBankAccountUseCase(iAuthRepo)

    @Provides
    fun provideUploadFileUseCase(iAuthRepo: IAuthRepo):IUploadFileUseCase = UploadFileUseCase(iAuthRepo)

}