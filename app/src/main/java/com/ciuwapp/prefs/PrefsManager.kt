package com.ciuwapp.prefs

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context){
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "csr-welcome"

    private val KEY_USERID = "userid"
    private val KEY_EMAIL = "email_address"
    private val KEY_PASSWORD = "userpassword"
    private val KEY_TOKEN = "token"

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)

    fun setEmail(value: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY_EMAIL, value)
        editor.commit()
    }
    fun getEmail(): String {
        return preferences.getString(KEY_EMAIL, "") ?: ""
    }
    fun setPassword(value: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY_PASSWORD, value)
        editor.commit()
    }
    fun getPassword(): String {
        return preferences.getString(KEY_PASSWORD, "") ?: ""
    }
    fun setUserId(value: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY_USERID, value)
        editor.commit()
    }
    fun getUserId(): String? {
        return preferences.getString(KEY_USERID, "")
    }
    fun setToken(value: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY_TOKEN, value)
        editor.commit()
    }
    fun getToken(): String {
        return preferences.getString(KEY_TOKEN, "") ?: ""
    }
    fun logout(){
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY_USERID, "")
        editor.putString(KEY_EMAIL, "")
        editor.commit()

    }
    fun clear() {
        val editor: SharedPreferences.Editor = preferences.edit()
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor.clear()
        editor.commit()
    }
    companion object {
        var instance: PrefsManager? = null
        @JvmStatic
        fun newInstance(context: Context): PrefsManager{
            if(instance == null){
                instance = PrefsManager(context)
            }
            return instance!!
        }
    }
}