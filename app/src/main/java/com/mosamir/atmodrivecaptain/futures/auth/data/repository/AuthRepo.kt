package com.mosamir.atmodrivecaptain.futures.auth.data.repository

import com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote.IAuthDataSource
import com.mosamir.atmodrivecaptain.futures.auth.data.model.RemoteCheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.FileUploadResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.util.IResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    ): IResult<CheckCodeResponse> {
        return iAuthDataSource.registerCaptain(mobile, avatar, deviceToken, deviceId, deviceType, nationalIdFront, nationalIdBack, drivingLicenseFront, drivingLicenseBack, isDarkMode)
    }

    override suspend fun registerVehicle(
        vehicleFront: String,
        vehicleBack: String,
        vehicleLeft: String,
        vehicleRight: String,
        vehicleFrontSeat: String,
        vehicleBackSeat: String,
        vehicleLicenseFront: String,
        vehicleLicenseBack: String
    ): IResult<CheckCodeResponse> {
        return iAuthDataSource.registerVehicle(vehicleFront, vehicleBack, vehicleLeft, vehicleRight, vehicleFrontSeat, vehicleBackSeat, vehicleLicenseFront, vehicleLicenseBack)
    }

    override suspend fun registerBankAccount(
        bankName: String,
        ibanNumber: String,
        accountName: String,
        accountNumber: String
    ): IResult<CheckCodeResponse> {
        return iAuthDataSource.registerBankAccount(bankName,ibanNumber,accountName,accountNumber)
    }

    override suspend fun uploadFile(
        part: MultipartBody.Part,
        path: RequestBody
    ): IResult<FileUploadResponse> {
        return iAuthDataSource.uploadFile(part,path)
    }


}