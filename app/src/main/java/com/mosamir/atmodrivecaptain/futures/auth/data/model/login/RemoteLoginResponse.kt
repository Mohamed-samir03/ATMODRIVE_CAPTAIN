package com.mosamir.atmodrivecaptain.futures.auth.data.model.login

data class RemoteLoginResponse(
    val `data`: RemoteDataLogin?,
    val message: String,
    val status: Boolean
)