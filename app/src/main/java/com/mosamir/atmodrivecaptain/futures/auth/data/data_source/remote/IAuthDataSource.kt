package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.FileUploadResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.login.LoginResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.util.IResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IAuthDataSource {

    suspend fun sendCode(mobile: String): IResult<LoginResponse>

    suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<LoginResponse>

    suspend fun registerCaptain(mobile:String,avatar:String,deviceToken:String,
                                deviceId:String,deviceType:String,nationalIdFront:String,
                                nationalIdBack:String,drivingLicenseFront:String,
                                drivingLicenseBack:String,isDarkMode:Int
    ): IResult<RegisterResponse>

    suspend fun registerVehicle(vehicleFront:String,vehicleBack:String,
                                vehicleLeft:String,vehicleRight:String,
                                vehicleFrontSeat:String,vehicleBackSeat:String,
                                vehicleLicenseFront:String,vehicleLicenseBack:String
    ): IResult<RegisterResponse>

    suspend fun registerBankAccount(bankName:String,ibanNumber:String,
                                    accountName:String,accountNumber:String
    ):IResult<RegisterResponse>

    suspend fun uploadFile(part: MultipartBody.Part,path: RequestBody): IResult<FileUploadResponse>

}