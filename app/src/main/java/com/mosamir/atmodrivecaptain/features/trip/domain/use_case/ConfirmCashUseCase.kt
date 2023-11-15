package com.mosamir.atmodrivecaptain.features.trip.domain.use_case

import com.mosamir.atmodrivecaptain.features.trip.domain.model.TripStatusResponse
import com.mosamir.atmodrivecaptain.features.trip.domain.repository.ITripRepo
import com.mosamir.atmodrivecaptain.util.IResult
import javax.inject.Inject

class ConfirmCashUseCase @Inject constructor(
    private val iTripRepo: ITripRepo
):IConfirmCashUseCase {
    override suspend fun confirmCash(tripId: Int, amount: Double): IResult<TripStatusResponse> {
        return iTripRepo.confirmCash(tripId, amount)
    }
}