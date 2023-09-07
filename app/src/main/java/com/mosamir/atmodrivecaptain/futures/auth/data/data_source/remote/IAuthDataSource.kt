package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote

import com.mosamir.atmodrivecaptain.futures.auth.data.model.RemoteCheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.FileUploadResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.util.IResult
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IAuthDataSource {

    suspend fun sendCode(mobile: String): IResult<SendCodeResponse>

    suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<CheckCodeResponse>

    suspend fun registerCaptain(mobile:String,avatar:String,deviceToken:String,
                                deviceId:String,deviceType:String,nationalIdFront:String,
                                nationalIdBack:String,drivingLicenseFront:String,
                                drivingLicenseBack:String,isDarkMode:Int
    ): IResult<CheckCodeResponse>

    suspend fun registerVehicle(vehicleFront:String,vehicleBack:String,
                                vehicleLeft:String,vehicleRight:String,
                                vehicleFrontSeat:String,vehicleBackSeat:String,
                                vehicleLicenseFront:String,vehicleLicenseBack:String
    ): IResult<CheckCodeResponse>

    suspend fun registerBankAccount(bankName:String,ibanNumber:String,
                                    accountName:String,accountNumber:String
    ):IResult<CheckCodeResponse>

    suspend fun uploadFile(part: MultipartBody.Part,path: RequestBody): IResult<FileUploadResponse>

}