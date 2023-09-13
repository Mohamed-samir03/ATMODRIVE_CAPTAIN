package com.mosamir.atmodrivecaptain.futures.auth.domain.use_case

import com.mosamir.atmodrivecaptain.futures.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.util.IResult

interface IRegisterBankAccountUseCase {

    suspend fun registerBankAccount(bankName:String,ibanNumber:String,
                                    accountName:String,accountNumber:String
    ): IResult<RegisterResponse>

}