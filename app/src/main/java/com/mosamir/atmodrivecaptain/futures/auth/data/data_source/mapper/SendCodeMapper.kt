package com.mosamir.atmodrivecaptain.futures.auth.data.data_source.mapper

import com.mosamir.atmodrivecaptain.futures.auth.data.model.RemoteSendCodeResponse
import com.mosamir.atmodrivecaptain.futures.auth.domain.model.SendCodeResponse

fun RemoteSendCodeResponse.asDomain() = SendCodeResponse(
    message,
    status
)