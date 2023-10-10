package com.mosamir.atmodrivecaptain.features.trip.presentation.common

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class OnlineCaptain(
    val id:String,
    val lat:String,
    val lng:String,
    val tripId:Int
){
    constructor():this("1","30.369852","30147852",0)
}
