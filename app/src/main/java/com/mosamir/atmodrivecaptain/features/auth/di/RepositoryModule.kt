package com.mosamir.atmodrivecaptain.features.auth.di

import com.mosamir.atmodrivecaptain.features.auth.data.data_source.remote.IAuthDataSource
import com.mosamir.atmodrivecaptain.features.auth.data.repository.AuthRepo
import com.mosamir.atmodrivecaptain.features.auth.domain.repository.IAuthRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Provides
    fun provideAuthRepo(iAuthDataSource: IAuthDataSource):IAuthRepo
            = AuthRepo(iAuthDataSource)


}