package com.mosamir.atmodrivecaptain.futures.auth.data.model

data class RemoteCheckCodeResponse(
    val `data`: RemoteData? = null,
    val message: String,
    val status: Int,
    val is_new: Boolean? = false
)