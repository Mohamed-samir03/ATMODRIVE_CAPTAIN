package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IAuthDataSource {

    suspend fun sendCode(mobile: String): IResult<SendCodeResponse>

    suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<CheckCodeResponse>

}