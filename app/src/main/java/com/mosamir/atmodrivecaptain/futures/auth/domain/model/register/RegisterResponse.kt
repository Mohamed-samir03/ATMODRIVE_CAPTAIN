package com.mosamir.atmodrivecaptain.futures.auth.domain.model.register

data class RegisterResponse(
    val `data`: DataRegister?,
    val message: String,
    val status: Boolean
)