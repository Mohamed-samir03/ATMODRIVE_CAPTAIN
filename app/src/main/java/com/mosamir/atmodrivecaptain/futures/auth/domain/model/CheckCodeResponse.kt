package com.mosamir.atmodrivecaptain.futures.auth.domain.model

data class CheckCodeResponse(
    val `data`: Data? = null,
    val message: String,
    val status: Int,
    val is_new: Boolean? = false
)