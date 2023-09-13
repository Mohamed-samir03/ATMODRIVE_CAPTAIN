package com.mosamir.atmodrivecaptain.futures.auth.domain.use_case

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IRegisterCaptainUseCase {

    suspend fun registerCaptain(mobile:String,avatar:String,deviceToken:String,
                                deviceId:String,deviceType:String,nationalIdFront:String,
                                nationalIdBack:String,drivingLicenseFront:String,
                                drivingLicenseBack:String,isDarkMode:Int
    ): IResult<RegisterResponse>

}