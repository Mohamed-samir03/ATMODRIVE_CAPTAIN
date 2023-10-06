package com.mosamir.atmodrivecaptain.features.auth.data.model.login

data class RemoteOptionsLogin(
    val brands: List<String>,
    val colors: List<String>,
    val device_types: List<String>,
    val gender: List<String>,
    val years: List<String>
)