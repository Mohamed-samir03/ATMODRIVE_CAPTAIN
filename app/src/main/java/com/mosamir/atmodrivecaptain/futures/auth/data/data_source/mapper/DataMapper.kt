package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper

import com.mosamir.atmodrivecaptain.futures.auth.data.model.RemoteData
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.Data

fun RemoteData.asDomain() =Data(
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