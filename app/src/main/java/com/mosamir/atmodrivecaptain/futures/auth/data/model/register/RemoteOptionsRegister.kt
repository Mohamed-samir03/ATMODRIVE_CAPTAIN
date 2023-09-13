package com.mosamir.atmodrivecaptain.futures.auth.data.model.register

data class RemoteOptionsRegister(
    val brands: List<String>,
    val colors: List<String>,
    val device_types: List<String>,
    val gender: List<String>,
    val years: List<String>
)