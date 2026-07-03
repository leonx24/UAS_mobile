package com.exampl.worksyncc.utils

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences("WorkSynccPrefs", Context.MODE_PRIVATE)

    companion object {
        private const val TOKEN_KEY = "jwt_token"
        private const val USER_ROLE = "user_role"
        private const val USER_NAME = "user_name"
    }

    fun saveUser(name: String, role: String, token: String) {
        val editor = prefs.edit()
        editor.putString(USER_NAME, name)
        editor.putString(USER_ROLE, role)
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    fun getUserRole(): String? = prefs.getString(USER_ROLE, null)
    fun getUserName(): String? = prefs.getString(USER_NAME, null)

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}
