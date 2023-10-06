package com.mosamir.atmodrivecaptain.features.auth.domain.use_case

import com.mosamir.atmodrivecaptain.features.auth.domain.model.register.RegisterResponse
import com.mosamir.atmodrivecaptain.features.auth.domain.repository.IAuthRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class RegisterBankAccountUseCase @Inject constructor(
    private val iAuthRepo: IAuthRepo
):IRegisterBankAccountUseCase {

    override suspend fun registerBankAccount(
        bankName: String,
        ibanNumber: String,
        accountName: String,
        accountNumber: String
    ): IResult<RegisterResponse> {
        return iAuthRepo.registerBankAccount(bankName,ibanNumber,accountName,accountNumber)
    }

}