package com.mosamir.atmodrivecaptain.futures.auth.data.model.login

data class RemoteDataLogin(
    val is_new: Boolean,
    val user: RemoteUserLogin?,
    val full_name:String?
)