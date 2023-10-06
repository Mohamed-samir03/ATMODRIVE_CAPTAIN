package com.mosamir.atmodrivecaptain.features.auth.data.model.login

data class RemoteDataLogin(
    val is_new: Boolean,
    val user: RemoteUserLogin?,
    val full_name:String?
)