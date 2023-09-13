package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote

import com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper.login.asDomain
import com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper.register.asDomain
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.FileUploadResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.login.LoginResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authApiService: AuthApiService,
    private val uploadFileApiService: UploadFileApiService
):IAuthDataSource {

    override suspend fun sendCode(mobile: String): IResult<LoginResponse> {
        return try {
            val sendCodeData = authApiService.sendCode(mobile)
            if (sendCodeData.status){
                return IResult.onSuccess(sendCodeData.asDomain())
            }else{
                return IResult.onFail(sendCodeData.message)
            }
        }catch (e: Exception){
             IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<LoginResponse> {
        return try {
            val checkCodeData = authApiService.checkCode(mobile,verificationCode, deviceToken)
            if (checkCodeData.status){
                return IResult.onSuccess(checkCodeData.asDomain())
            }else{
                return IResult.onFail(checkCodeData.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
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
    ): IResult<RegisterResponse> {
        return try {
            val captainData = authApiService.registerCaptain(mobile, avatar, deviceToken, deviceId, deviceType, nationalIdFront, nationalIdBack, drivingLicenseFront, drivingLicenseBack, isDarkMode)
            if (captainData.status){
                return IResult.onSuccess(captainData.asDomain())
            }else{
                return IResult.onFail(captainData.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
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
    ): IResult<RegisterResponse> {
        return try {
            val vehicleData = authApiService.registerVehicle(vehicleFront, vehicleBack, vehicleLeft, vehicleRight, vehicleFrontSeat, vehicleBackSeat, vehicleLicenseFront, vehicleLicenseBack)
            if (vehicleData.status){
                return IResult.onSuccess(vehicleData.asDomain())
            }else{
                return IResult.onFail(vehicleData.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun registerBankAccount(
        bankName: String,
        ibanNumber: String,
        accountName: String,
        accountNumber: String
    ): IResult<RegisterResponse> {
        return try {
            val bankAccountData = authApiService.registerBankAccount(bankName,ibanNumber, accountName, accountNumber)
            if (bankAccountData.status){
                return IResult.onSuccess(bankAccountData.asDomain())
            }else{
                return IResult.onFail(bankAccountData.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun uploadFile(
        part: MultipartBody.Part,
        path: RequestBody
    ): IResult<FileUploadResponse> {
        return try {
            val uploadData = uploadFileApiService.uploadFile(part,path)
            if (uploadData.status == 1){
                return IResult.onSuccess(uploadData)
            }else{
                return IResult.onFail(uploadData.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

}