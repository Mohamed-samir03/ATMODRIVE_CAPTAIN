package com.mosamir.atmodrivecaptain.futures.auth.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.ICheckCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.IRegisterBankAccountUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.IRegisterCaptainUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.IRegisterVehicleUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.ISendCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.IUploadFileUseCase
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.getError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val iSendCodeUseCase: ISendCodeUseCase,
    private val iCheckCodeUseCase: ICheckCodeUseCase,
    private val iRegisterCaptainUseCase: IRegisterCaptainUseCase,
    private val iRegisterVehicleUseCase: IRegisterVehicleUseCase,
    private val iRegisterBankAccountUseCase: IRegisterBankAccountUseCase,
    private val iUploadFileUseCase: IUploadFileUseCase
):ViewModel() {

    private val _sendCodeResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val sendCodeResult: StateFlow<NetworkState?> =_sendCodeResult

    private val _checkCodeResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val checkCodeResult:StateFlow<NetworkState?> = _checkCodeResult

    private val _registerCaptainResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val registerCaptainResult:StateFlow<NetworkState?> = _registerCaptainResult

    private val _uploadFileResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val uploadFileResult:StateFlow<NetworkState?> = _uploadFileResult

    private val _registerVehicleResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val registerVehicleResult:StateFlow<NetworkState?> = _registerVehicleResult

    private val _registerBankAccountResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val registerBankAccountResult:StateFlow<NetworkState?> = _registerBankAccountResult


    fun sendCode(mobile: String) {
        _sendCodeResult.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iSendCodeUseCase.sendCode(mobile)
                if (result.isSuccessful()){
                    _sendCodeResult.value = NetworkState.getLoaded(result)
                }else{
                    _sendCodeResult.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _sendCodeResult.value = NetworkState.getErrorMessage(ex)
            }
        }
    }


    fun checkCode(mobile: String, verificationCode: String,deviceToken: String) {
        _checkCodeResult.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iCheckCodeUseCase.checkCode(mobile,verificationCode, deviceToken)
                if (result.isSuccessful()){
                    _checkCodeResult.value = NetworkState.getLoaded(result)
                }else{
                    _checkCodeResult.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _checkCodeResult.value = NetworkState.getErrorMessage(ex)
            }
        }
    }


    fun registerCaptain(mobile:String,avatar:String,deviceToken:String,
                        deviceId:String,deviceType:String,nationalIdFront:String,
                        nationalIdBack:String,drivingLicenseFront:String,
                        drivingLicenseBack:String,isDarkMode:Int
    ) {
        _registerCaptainResult.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iRegisterCaptainUseCase.registerCaptain(mobile, avatar, deviceToken, deviceId, deviceType, nationalIdFront, nationalIdBack, drivingLicenseFront, drivingLicenseBack, isDarkMode)
                if (result.isSuccessful()){
                    _registerCaptainResult.value = NetworkState.getLoaded(result)
                }else{
                    _registerCaptainResult.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _registerCaptainResult.value = NetworkState.getErrorMessage(ex)
            }
        }
    }


    fun registerVehicle(vehicleFront:String,vehicleBack:String,
                        vehicleLeft:String,vehicleRight:String,
                        vehicleFrontSeat:String,vehicleBackSeat:String,
                        vehicleLicenseFront:String,vehicleLicenseBack:String
    ){
        _registerVehicleResult.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iRegisterVehicleUseCase.registerVehicle(vehicleFront, vehicleBack, vehicleLeft, vehicleRight, vehicleFrontSeat, vehicleBackSeat, vehicleLicenseFront, vehicleLicenseBack)
                if (result.isSuccessful()){
                    _registerVehicleResult.value = NetworkState.getLoaded(result)
                }else{
                    _registerVehicleResult.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _registerVehicleResult.value = NetworkState.getErrorMessage(ex)
            }
        }

    }


    fun registerBankAccount(bankName:String,ibanNumber:String,
                            accountName:String,accountNumber:String) {
        _registerBankAccountResult.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iRegisterBankAccountUseCase.registerBankAccount(bankName,ibanNumber,accountName,accountNumber)
                if (result.isSuccessful()){
                    _registerBankAccountResult.value = NetworkState.getLoaded(result)
                }else{
                    _registerBankAccountResult.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _registerBankAccountResult.value = NetworkState.getErrorMessage(ex)
            }
        }
    }


    fun uploadFile(part: MultipartBody.Part, path: RequestBody) {
        _uploadFileResult.value = NetworkState.LOADING
        viewModelScope.launch {
            try {
                val result = iUploadFileUseCase.uploadFile(part,path)
                if (result.isSuccessful()){
                    _uploadFileResult.value = NetworkState.getLoaded(result)
                }else{
                    _uploadFileResult.value = NetworkState.getErrorMessage(result.getError().toString())
                }
            }catch (ex:Exception){
                ex.printStackTrace()
                _uploadFileResult.value = NetworkState.getErrorMessage(ex)
            }
        }
    }


}