package com.mosamir.atmodrivecaptain.futures.auth.presentation.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.ICheckCodeUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.IRegisterCaptainUseCase
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.ISendCodeUseCase
import com.mosamir.atmodrivecaptain.util.NetworkState
import com.mosamir.atmodrivecaptain.util.getError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val iSendCodeUseCase: ISendCodeUseCase,
    private val iCheckCodeUseCase: ICheckCodeUseCase,
    private val iRegisterCaptainUseCase: IRegisterCaptainUseCase
):ViewModel() {


    private val _sendCodeResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val sendCodeResult: StateFlow<NetworkState?> =_sendCodeResult

    private val _checkCodeResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val checkCodeResult:StateFlow<NetworkState?> = _checkCodeResult

    private val _registerCaptainResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val registerCaptainResult:StateFlow<NetworkState?> = _registerCaptainResult


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


}