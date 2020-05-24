package com.ciuwapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.ciuwapp.R
import com.ciuwapp.api.ClientAPIService

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
            ClientAPIService.requestLogin(email!!, password!!) { succeeded, result ->
                if (succeeded == 200) {
                    val userData: UserData? = result
                    if (userData?.token != null) {
                        AppHelper.userProfile = userData.user
                        PrefsManager.newInstance(this)?.setToken(userData?.token)
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
    private fun launchHomeActivity() {
        var intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchLoginActivity(){
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

}
