package com.mosamir.atmodrivecaptain.futures.auth.domain.use_case

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.login.LoginResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class CheckCodeUseCase @Inject constructor(
    private val iAuthRepo: IAuthRepo
):ICheckCodeUseCase {
    override suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<LoginResponse> {
        return iAuthRepo.checkCode(mobile,verificationCode, deviceToken)
    }
}