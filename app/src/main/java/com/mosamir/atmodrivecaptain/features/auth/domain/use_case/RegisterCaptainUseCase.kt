package com.mosamir.atmodrivecaptain.features.auth.domain.use_case

import com.mosamir.atmodrivecaptain.features.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.features.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class RegisterCaptainUseCase @Inject constructor(
    private val iAuthRepo: IAuthRepo
):IRegisterCaptainUseCase {
    override suspend fun registerCaptain(
        mobile: String,
        avatar: String,
        deviceToken: String,
        deviceId: String,
        deviceType: String,
        nationalIdFront: String,
        nationalIdBack: String,
        drivingLicenseFront: String,
        drivingLicenseBack: String,
        isDarkMode: Int
    ): IResult<RegisterResponse> {
        return iAuthRepo.registerCaptain(mobile, avatar, deviceToken, deviceId, deviceType, nationalIdFront, nationalIdBack, drivingLicenseFront, drivingLicenseBack, isDarkMode)
    }
}