package com.atianza.tellmesdk

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesManager {
    private const val APP_SETTINGS = "APP_SETTINGS"
    private const val SOME_STRING_VALUE = "TellMeToken"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE)
    }

    fun getSomeStringValue(context: Context): String? {
        return getSharedPreferences(context).getString(SOME_STRING_VALUE, null)
    }

    fun setSomeStringValue(context: Context, newValue: String?) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(SOME_STRING_VALUE, newValue)
        editor.commit()
    } // other getters/setters

    fun clearSharedPreferences(context: Context) {
        getSharedPreferences(context).edit().clear().commit()
    }
}