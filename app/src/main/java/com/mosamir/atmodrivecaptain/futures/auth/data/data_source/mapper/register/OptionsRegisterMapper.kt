package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper.register

import com.mosamir.atmodrivecaptain.futures.auth.data.model.register.RemoteOptionsRegister
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.register.OptionsRegister

fun RemoteOptionsRegister.asDomain() = OptionsRegister(
    brands,
    colors,
    device_types,
    gender,
    years
)