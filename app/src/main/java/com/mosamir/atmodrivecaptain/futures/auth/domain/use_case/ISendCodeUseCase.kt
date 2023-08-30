package com.mosamir.atmodrivecaptain.futures.auth.domain.use_case

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface ISendCodeUseCase {

    suspend fun sendCode(mobile: String): IResult<SendCodeResponse>

}