package com.mosamir.atmodrivecaptain.features.auth.domain.model.login

data class UserLogin(
    val id:Int,
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
    val options: OptionsLogin,
    val register_step: Int,
    val remember_token: String,
    val status: Int
)