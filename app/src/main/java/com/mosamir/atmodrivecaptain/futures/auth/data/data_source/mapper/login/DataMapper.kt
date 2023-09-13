package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper.login

import com.mosamir.atmodrivecaptain.futures.auth.data.model.login.RemoteDataLogin
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.login.DataLogin

fun RemoteDataLogin.asDomain() = DataLogin(
    is_new,
    user?.asDomain(),
    full_name
)