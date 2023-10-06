package com.mosamir.atmodrivecaptain.features.auth.data.data_source.mapper.login

import com.mosamir.atmodrivecaptain.features.auth.data.model.login.RemoteLoginResponse
import com.mosamir.atmodrivecaptain.features.auth.domain.model.login.LoginResponse

fun RemoteLoginResponse.asDomain() = LoginResponse(
    data?.asDomain(),
    message,
    status
)