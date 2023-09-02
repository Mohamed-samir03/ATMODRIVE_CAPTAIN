package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote

import com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper.asDomain
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse
import com.mosamir.atmodrivecaptain.util.IResult
import com.mosamir.atmodrivecaptain.util.NetworkState
import java.lang.Exception
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authApiService: AuthApiService
):IAuthDataSource {

    override suspend fun sendCode(mobile: String): IResult<SendCodeResponse> {
        return try {
            val sendCodeData = authApiService.sendCode(mobile)
            if (sendCodeData.status == 1){
                return IResult.onSuccess(sendCodeData.asDomain())
            }else{
                return IResult.onFail(sendCodeData.message)
            }
        }catch (e: Exception){
             IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

    override suspend fun checkCode(mobile:String,verificationCode:String, deviceToken:String): IResult<CheckCodeResponse> {
        return try {
            val checkCodeData = authApiService.checkCode(mobile,verificationCode, deviceToken)
            if (checkCodeData.status == 1){
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
    ): IResult<CheckCodeResponse> {
        return try {
            val captainData = authApiService.registerCaptain(mobile, avatar, deviceToken, deviceId, deviceType, nationalIdFront, nationalIdBack, drivingLicenseFront, drivingLicenseBack, isDarkMode)
            if (captainData.status == 1){
                return IResult.onSuccess(captainData.asDomain())
            }else{
                return IResult.onFail(captainData.message)
            }
        }catch (e: Exception){
            IResult.onFail(NetworkState.getErrorMessage(e).msg.toString())
        }
    }

}