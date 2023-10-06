package com.mosamir.atmodrivecaptain.features.auth.data.data_source.remote

import com.mosamir.atmodrivecaptain.features.auth.domain.model.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadFileApiService {

    @Multipart
    @POST("api/upload-files")
    suspend fun uploadFile(
        @Part part: MultipartBody.Part,
        @Part("path") path: RequestBody
    ): FileUploadResponse

}