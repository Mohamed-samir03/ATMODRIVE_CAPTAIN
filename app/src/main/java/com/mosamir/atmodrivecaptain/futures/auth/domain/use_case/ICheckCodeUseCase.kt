package com.mosamir.atmodrivecaptain.futures.auth.domain.use_case

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.login.LoginResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface ICheckCodeUseCase {

    suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<LoginResponse>

}