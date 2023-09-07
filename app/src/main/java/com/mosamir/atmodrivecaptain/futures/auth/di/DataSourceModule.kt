package com.mosamir.atmodrivecaptain.futures.auth.di

import com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote.AuthApiService
import com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote.AuthDataSource
import com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote.IAuthDataSource
import com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote.UploadFileApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {



    @Provides
    fun getAuthDataSource(authApiService: AuthApiService,uploadFileApiService: UploadFileApiService):IAuthDataSource
            = AuthDataSource(authApiService,uploadFileApiService)


}