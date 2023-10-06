package com.mosamir.atmodrivecaptain.features.auth.data.data_source.mapper.login

import com.mosamir.atmodrivecaptain.features.auth.data.model.login.RemoteOptionsLogin
import com.mosamir.atmodrivecaptain.features.auth.domain.model.login.OptionsLogin


fun RemoteOptionsLogin.asDomain() = OptionsLogin(
    brands,
    colors,
    device_types,
    gender,
    years
)