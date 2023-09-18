package com.mosamir.atmodrivecaptain.futures.auth.data.model.register

data class RemoteDataRegister(
    val avatar: String,
    val birthday: Any?,
    val captain_code: String,
    val email: Any?,
    val full_name: Any?,
    val gender: Any?,
    val is_active: Int,
    val is_dark_mode: Int,
    val lang: String,
    val mobile: String,
    val nationality: Any?,
    val options: RemoteOptionsRegister,
    val register_step: Int,
    val remember_token: String,
    val status: Int
)