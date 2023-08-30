package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper

import com.mosamir.atmodrivecaptain.futures.auth.data.model.RemoteCheckCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.CheckCodeResponse

fun RemoteCheckCodeResponse.asDomain() = CheckCodeResponse(
    data?.asDomain(),
    message,
    status,
    is_new
)