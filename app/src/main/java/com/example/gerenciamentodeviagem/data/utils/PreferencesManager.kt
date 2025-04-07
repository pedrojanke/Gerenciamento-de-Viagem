package com.example.gerenciamentodeviagem.data.utils

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private const val PREF_NAME = "user_prefs"
    private const val USER_ID_KEY = "user_id"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserId(context: Context, userId: Int) {
        val editor = getPrefs(context).edit()
        editor.putInt(USER_ID_KEY, userId)
        editor.apply()
    }

    fun getUserId(context: Context): Int {
        return getPrefs(context).getInt(USER_ID_KEY, -1)
    }

    fun clearUserId(context: Context) {
        getPrefs(context).edit().remove(USER_ID_KEY).apply()
    }
}
