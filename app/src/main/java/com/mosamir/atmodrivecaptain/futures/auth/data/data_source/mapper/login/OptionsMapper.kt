package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper.login

import com.mosamir.atmodrivecaptain.futures.auth.data.model.login.RemoteOptionsLogin
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.login.OptionsLogin


fun RemoteOptionsLogin.asDomain() = OptionsLogin(
    brands,
    colors,
    device_types,
    gender,
    years
)