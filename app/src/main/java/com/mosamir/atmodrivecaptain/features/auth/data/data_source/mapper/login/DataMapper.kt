package com.mosamir.atmodrivecaptain.features.auth.data.data_source.mapper.login

import com.mosamir.atmodrivecaptain.features.auth.data.model.login.RemoteDataLogin
import com.mosamir.atmodrivecaptain.features.auth.domain.model.login.DataLogin

fun RemoteDataLogin.asDomain() = DataLogin(
    is_new,
    user?.asDomain(),
    full_name
)