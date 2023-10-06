package com.mosamir.atmodrivecaptain.features.auth.domain.use_case

import com.mosamir.atmodrivecaptain.features.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.features.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class RegisterVehicleUseCase @Inject constructor(
    private val iAuthRepo: IAuthRepo
):IRegisterVehicleUseCase {

    override suspend fun registerVehicle(
        vehicleFront: String,
        vehicleBack: String,
        vehicleLeft: String,
        vehicleRight: String,
        vehicleFrontSeat: String,
        vehicleBackSeat: String,
        vehicleLicenseFront: String,
        vehicleLicenseBack: String
    ): IResult<RegisterResponse> {
        return iAuthRepo.registerVehicle(vehicleFront, vehicleBack, vehicleLeft, vehicleRight, vehicleFrontSeat, vehicleBackSeat, vehicleLicenseFront, vehicleLicenseBack)
    }

}