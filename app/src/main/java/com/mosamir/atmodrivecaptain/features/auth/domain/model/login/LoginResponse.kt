package com.mosamir.atmodrivecaptain.features.auth.domain.model.login

data class LoginResponse(
    val `data`: DataLogin?,
    val message: String,
    val status: Boolean
)