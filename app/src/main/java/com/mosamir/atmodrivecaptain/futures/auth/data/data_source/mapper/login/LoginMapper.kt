package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper.login

import com.mosamir.atmodrivecaptain.futures.auth.data.model.login.RemoteLoginResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.login.LoginResponse

fun RemoteLoginResponse.asDomain() = LoginResponse(
    data?.asDomain(),
    message,
    status
)