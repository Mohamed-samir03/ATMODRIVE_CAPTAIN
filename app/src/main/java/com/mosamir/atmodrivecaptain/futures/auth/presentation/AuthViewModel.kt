package com.mosamir.atmodrivecaptain.futures.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mosamir.atmodrivecaptain.futures.auth.domain.use_case.ICheckCodeUseCase
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
    private val iCheckCodeUseCase: ICheckCodeUseCase
):ViewModel() {


    private val _sendCodeResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val sendCodeResult: StateFlow<NetworkState?> =_sendCodeResult

    private val _checkCodeResult: MutableStateFlow<NetworkState?> = MutableStateFlow(null)
    val checkCodeResult:StateFlow<NetworkState?> = _checkCodeResult



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


}