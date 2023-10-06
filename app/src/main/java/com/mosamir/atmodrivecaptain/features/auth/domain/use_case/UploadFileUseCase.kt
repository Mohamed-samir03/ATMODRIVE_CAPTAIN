package com.mosamir.atmodrivecaptain.features.auth.domain.use_case

import com.mosamir.atmodrivecaptain.features.auth.domain.model.FileUploadResponse
import com.mosamir.atmodrivecaptain.features.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.util.IResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val iAuthRepo: IAuthRepo
):IUploadFileUseCase {
    override suspend fun uploadFile(
        part: MultipartBody.Part,
        path: RequestBody
    ): IResult<FileUploadResponse> {
        return iAuthRepo.uploadFile(part,path)
    }
}