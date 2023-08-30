package com.mosamir.atmodrivecaptain.futures.auth.data.repository

import com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote.IAuthDataSource
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class AuthRepo @Inject constructor(
    private val iAuthDataSource: IAuthDataSource
) :IAuthRepo{


    override suspend fun sendCode(mobile: String): IResult<SendCodeResponse> {
        return iAuthDataSource.sendCode(mobile)
    }

    override suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<CheckCodeResponse> {
        return  iAuthDataSource.checkCode(mobile,verificationCode, deviceToken)
    }



}