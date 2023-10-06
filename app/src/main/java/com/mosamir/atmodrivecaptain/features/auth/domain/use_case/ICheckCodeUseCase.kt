package com.mosamir.atmodrivecaptain.features.auth.domain.use_case

import com.mosamir.atmodrivecaptain.features.auth.domain.model.login.LoginResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface ICheckCodeUseCase {

    suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<LoginResponse>

}