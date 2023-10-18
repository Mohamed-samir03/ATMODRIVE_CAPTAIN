package com.mosamir.atmodrivecaptain.features.auth.data.data_source.mapper.login

import com.mosamir.atmodrivecaptain.features.auth.data.model.login.RemoteUserLogin
import com.mosamir.atmodrivecaptain.features.auth.domain.model.login.UserLogin

fun RemoteUserLogin.asDomain() = UserLogin(
    id,
    avatar,
    birthday,
    captain_code,
    email,
    full_name,
    gender,
    is_active,
    is_dark_mode,
    lang,
    mobile,
    nationality,
    options.asDomain(),
    register_step,
    remember_token,
    status
)