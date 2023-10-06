package com.mosamir.atmodrivecaptain.features.auth.domain.model.login

data class OptionsLogin(
    val brands: List<String>,
    val colors: List<String>,
    val device_types: List<String>,
    val gender: List<String>,
    val years: List<String>
)