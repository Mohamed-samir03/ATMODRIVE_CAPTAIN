package com.mosamir.atmodrivecaptain.util

import com.google.android.gms.maps.model.LatLng

class Constants {

    companion object{

        const val BASE_URL = "https://s1.drive.api.atmosphere.solutions/api/v1/captains/"
        const val UPLOAD_FILES_BASE_URL = "https://s1.drive.dashboard.atmosphere.solutions/"


        const val VEHICLE_IMAGE_PATH = "captains"

        const val CAPTAIN_STATUS = "captain_status"
        var isBottomSheetOn = false
        var captainLatLng:LatLng? = null
        var pickUpLatLng: LatLng? = null
        var dropOffLatLng: LatLng? = null

        // SharedPreferences keys
        const val CAPTAIN_ID_PREFS = "CaptainID"
        const val CAPTAIN_PREFS = "CaptainPrefs"
        const val AVATAR_PREFS = "avatar"
        const val BIRTHDAY_PREFS = "birthday"
        const val CAPTAIN_CODE_PREFS = "captain_code"
        const val EMAIL_PREFS = "email"
        const val FULL_NAME_PREFS = "full_name"
        const val GENDER_PREFS = "gender"
        const val IS_ACTIVE_PREFS = "is_active"
        const val IS_DARK_MODE_PREFS = "is_dark_mode"
        const val LANG_PREFS = "lang"
        const val MOBILE_PREFS = "mobile"
        const val NATIONALITY_PREFS = "nationality"
        const val REGISTER_STEP_PREFS = "register_step"
        const val REMEMBER_TOKEN_PREFS = "remember_token"
        const val STATUS_PREFS = "status"
        const val TRIP_COST = "trip_cost"

    }

}