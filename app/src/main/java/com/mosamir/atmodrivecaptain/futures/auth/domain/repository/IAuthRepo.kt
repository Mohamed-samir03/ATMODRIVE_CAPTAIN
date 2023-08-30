package com.mosamir.atmodrivecaptain.futures.auth.domain.repository

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IAuthRepo {

    suspend fun sendCode(mobile: String): IResult<SendCodeResponse>

    suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<CheckCodeResponse>

}