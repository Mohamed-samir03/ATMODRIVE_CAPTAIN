package com.mosamir.atmodrivecaptain.features.auth.data.data_source.mapper.register

import com.mosamir.atmodrivecaptain.features.auth.data.model.register.RemoteRegisterResponse
import com.mosamir.atmodrivecaptain.features.auth.domain.model.register.RegisterResponse

fun RemoteRegisterResponse.asDomain() = RegisterResponse(
    data?.asDomain(),
    message,
    status
)