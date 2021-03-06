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
    private val KEY_FCM_TOKEN = "fcm_token"
    private val KEY_BADGE_EVENT = "badge_event"
    private val KEY_BADGE_MESSAGE = "badge_message"
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
    fun setFCMToken(value: String) {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(KEY_FCM_TOKEN, value)
        editor.commit()
    }
    fun getFCMToken(): String {
        return preferences.getString(KEY_FCM_TOKEN, "") ?: ""
    }
    fun setEventBadges(value: Int){
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putInt(KEY_BADGE_EVENT, value)
        editor.commit()
    }

    fun getEventBadges(): Int{
        return preferences.getInt(KEY_BADGE_EVENT, 0)
    }
    fun setMessageBadges(value: Int){
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putInt(KEY_BADGE_MESSAGE, value)
        editor.commit()
    }

    fun getMessageBadges(): Int{
        return preferences.getInt(KEY_BADGE_MESSAGE, 0)
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