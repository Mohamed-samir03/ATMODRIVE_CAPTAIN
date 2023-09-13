package com.mosamir.atmodrivecaptain.futures.auth.domain.use_case

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IRegisterVehicleUseCase {

    suspend fun registerVehicle(vehicleFront:String,vehicleBack:String,
                                vehicleLeft:String,vehicleRight:String,
                                vehicleFrontSeat:String,vehicleBackSeat:String,
                                vehicleLicenseFront:String,vehicleLicenseBack:String
    ): IResult<RegisterResponse>

}