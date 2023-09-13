package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.remote

import com.mosamir.atmodrivecaptain.futures.auth.data.model.login.RemoteLoginResponse
import com.mosamir.atmodrivecaptain.futures.auth.data.model.register.RemoteRegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


const val SEND_CODE = "send-code"
const val CHECK_CODE = "check-code"
const val REGISTER_CAPTAIN = "register-captain"
const val REGISTER_VEHICLE = "register-vehicle"
const val REGISTER_BANK_ACCOUNT = "register-bank-account"

interface AuthApiService {

    @POST(SEND_CODE)
    @FormUrlEncoded
    suspend fun sendCode(@Field("mobile") mobile: String):RemoteLoginResponse


    @POST(CHECK_CODE)
    @FormUrlEncoded
    suspend fun checkCode(@Field("mobile") mobile:String,
                          @Field("verification_code") verificationCode:String,
                          @Field("device_token") deviceToken:String
      ):RemoteLoginResponse


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
        ):RemoteRegisterResponse


    @POST(REGISTER_VEHICLE)
    @FormUrlEncoded
    suspend fun registerVehicle(@Field("vehicle_front") vehicleFront:String,
                                @Field("vehicle_back") vehicleBack:String,
                                @Field("vehicle_left") vehicleLeft:String,
                                @Field("vehicle_right") vehicleRight:String,
                                @Field("vehicle_front_seat") vehicleFrontSeat:String,
                                @Field("vehicle_back_seat") vehicleBackSeat:String,
                                @Field("vehicle_license_front") vehicleLicenseFront:String,
                                @Field("vehicle_license_back") vehicleLicenseBack:String
    ):RemoteRegisterResponse


    @POST(REGISTER_BANK_ACCOUNT)
    @FormUrlEncoded
    suspend fun registerBankAccount(@Field("bank_name") bankName:String,
                                    @Field("iban_number") ibanNumber:String,
                                    @Field("account_name") accountName:String,
                                    @Field("account_number") accountNumber:String
    ):RemoteRegisterResponse

}