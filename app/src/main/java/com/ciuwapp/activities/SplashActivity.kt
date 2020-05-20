package com.ciuwapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.ciuwapp.R
import com.ciuwapp.api.ClientLoginService

import com.ciuwapp.data.UserData
import com.ciuwapp.helpers.AppHelper
import com.ciuwapp.prefs.PrefsManager


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        var email = PrefsManager.newInstance(this).getEmail()
        var password = PrefsManager.newInstance(this).getPassword()

        if (email?.isEmpty() or password?.isEmpty()) {
            Handler().postDelayed({
                launchLoginActivity()
            }, 2000)
        } else {
            ClientLoginService.requestLogin(email!!, password!!) { succeeded, result ->
                if (succeeded) {
                    val userData: UserData? = result
                    if (userData?.token != null) {
                        AppHelper.userProfile = userData.user
                        launchHomeActivity()
                    } else {
                        launchLoginActivity()
                    }
                } else {
                    launchLoginActivity()
                }
            }
        }
    }
    fun launchHomeActivity() {
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun launchLoginActivity(){
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
