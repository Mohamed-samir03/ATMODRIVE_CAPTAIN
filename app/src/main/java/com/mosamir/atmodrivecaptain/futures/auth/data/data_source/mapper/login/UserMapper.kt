package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper.login

import com.mosamir.atmodrivecaptain.futures.auth.data.model.login.RemoteUserLogin
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.login.UserLogin

fun RemoteUserLogin.asDomain() = UserLogin(
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