package com.mosamir.atmodrivecaptain.di

import android.content.Context
import com.mosamir.atmodrivecaptain.features.auth.data.data_source.remote.AuthApiService
import com.mosamir.atmodrivecaptain.features.auth.data.data_source.remote.UploadFileApiService
import com.mosamir.atmodrivecaptain.util.Constants
import com.mosamir.atmodrivecaptain.util.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    @Named("apiCaptain")
    fun provideRetrofit(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .callTimeout(50, TimeUnit.SECONDS)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val originalUrl = originalRequest.url
                    val url = originalUrl.newBuilder().build()
                    val requestBuilder = originalRequest.newBuilder().url(url)
                        .addHeader("Accept", "application/json")
                        .addHeader("Authorization", "Bearer ${SharedPreferencesManager(context).getString(
                            Constants.REMEMBER_TOKEN_PREFS)}"
                        )
                    val request = requestBuilder.build()
                    val response = chain.proceed(request)
                    response.code//status code
                    return response
                }
            })
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("UploadFile")
    fun provideRetrofitForUpload(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .callTimeout(50, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.UPLOAD_FILES_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApiService(@Named("apiCaptain") retrofit: Retrofit):AuthApiService
            = retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideUploadFileApiService(@Named("UploadFile") retrofit: Retrofit):UploadFileApiService
            = retrofit.create(UploadFileApiService::class.java)


    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

}