package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper

import com.mosamir.atmodrivecaptain.futures.auth.data.model.RemoteOptions
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.Options

fun RemoteOptions.asDomain() = Options(
    brands,
    colors,
    device_types,
    gender,
    years
)