package com.mosamir.atmodrivecaptain.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager (context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.CAPTAIN_PREFS, Context.MODE_PRIVATE)

    public fun saveString(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    public fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    public fun getString(key: String, defaultValue: String = ""): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    public fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue) ?: defaultValue
    }

    public fun clearString(key: String) {
        sharedPreferences.edit()
            .remove(key)
            .apply()
    }

}