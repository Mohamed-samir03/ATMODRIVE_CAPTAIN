package com.mosamir.atmodrivecaptain.futures.auth.domain.repository

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IAuthRepo {

    suspend fun sendCode(mobile: String): IResult<SendCodeResponse>

    suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<CheckCodeResponse>

    suspend fun registerCaptain(mobile:String,avatar:String,deviceToken:String,
                                deviceId:String,deviceType:String,nationalIdFront:String,
                                nationalIdBack:String,drivingLicenseFront:String,
                                drivingLicenseBack:String,isDarkMode:Int
    ): IResult<CheckCodeResponse>

}