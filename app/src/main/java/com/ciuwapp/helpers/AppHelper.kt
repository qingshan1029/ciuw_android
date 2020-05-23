package com.ciuwapp.helpers

import com.ciuwapp.data.UserProfile

class AppHelper {
    companion object {
        var userProfile: UserProfile? = null

        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}