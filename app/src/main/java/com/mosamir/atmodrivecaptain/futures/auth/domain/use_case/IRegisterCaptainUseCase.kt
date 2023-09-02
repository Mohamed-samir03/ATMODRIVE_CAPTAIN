package com.mosamir.atmodrivecaptain.futures.auth.domain.use_case

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IRegisterCaptainUseCase {

    suspend fun registerCaptain(mobile:String,avatar:String,deviceToken:String,
                                deviceId:String,deviceType:String,nationalIdFront:String,
                                nationalIdBack:String,drivingLicenseFront:String,
                                drivingLicenseBack:String,isDarkMode:Int
    ): IResult<CheckCodeResponse>

}