package com.mosamir.atmodrivecaptain.futures.auth.domain.use_case

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.FileUploadResponse
import com.mosamir.atmodrivecaptain.util.IResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IUploadFileUseCase {

    suspend fun uploadFile(part: MultipartBody.Part, path: RequestBody): IResult<FileUploadResponse>

}