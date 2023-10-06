package com.mosamir.atmodrivecaptain.features.auth.data.data_source.mapper.register

import com.mosamir.atmodrivecaptain.features.auth.data.model.register.RemoteDataRegister
import com.mosamir.atmodrivecaptain.features.auth.domain.model.register.DataRegister

fun RemoteDataRegister.asDomain() = DataRegister(
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