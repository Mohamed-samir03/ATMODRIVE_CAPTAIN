package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote

import com.mosamir.atmodrivecaptain.futures.auth.data.model.RemoteCheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.data.model.RemoteSendCodeResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


const val SEND_CODE = "send-code"
const val CHECK_CODE = "check-code"
const val REGISTER_CAPTAIN = "register-captain"

interface AuthApiService {

    @POST(SEND_CODE)
    @FormUrlEncoded
    suspend fun sendCode(@Field("mobile") mobile: String):RemoteSendCodeResponse


    @POST(CHECK_CODE)
    @FormUrlEncoded
    suspend fun checkCode(@Field("mobile") mobile:String,
                          @Field("verification_code") verificationCode:String,
                          @Field("device_token") deviceToken:String
      ):RemoteCheckCodeResponse


    @POST(REGISTER_CAPTAIN)
    @FormUrlEncoded
    suspend fun registerCaptain(@Field("mobile") mobile:String,
                             @Field("avatar") avatar:String,
                             @Field("device_token") deviceToken:String,
                             @Field("device_id") deviceId:String,
                             @Field("device_type") deviceType:String,
                             @Field("national_id_front") nationalIdFront:String,
                             @Field("national_id_back") nationalIdBack:String,
                             @Field("driving_license_front") drivingLicenseFront:String,
                             @Field("driving_license_back") drivingLicenseBack:String,
                             @Field("is_dark_mode") isDarkMode:Int
        ):RemoteCheckCodeResponse

}