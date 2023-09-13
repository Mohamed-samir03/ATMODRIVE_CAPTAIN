package com.mosamir.atmodrivecaptain.futures.auth.domain.use_case

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.login.LoginResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class SendCodeUseCase @Inject constructor(
    private val iAuthRepo: IAuthRepo
):ISendCodeUseCase {
    override suspend fun sendCode(mobile: String): IResult<LoginResponse> {
        return iAuthRepo.sendCode(mobile)
    }
}