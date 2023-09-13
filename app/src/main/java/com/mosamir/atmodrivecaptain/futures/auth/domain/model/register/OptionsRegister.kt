package com.mosamir.atmodrivecaptain.futures.auth.domain.model.register

data class OptionsRegister(
    val brands: List<String>,
    val colors: List<String>,
    val device_types: List<String>,
    val gender: List<String>,
    val years: List<String>
)