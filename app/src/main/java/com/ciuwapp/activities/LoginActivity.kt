package com.ciuwapp.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView

import com.ciuwapp.R
import com.ciuwapp.api.ClientAPIService

import com.ciuwapp.data.UserData

import kotlinx.android.synthetic.main.activity_login.*
import com.ciuwapp.helpers.AppHelper
import com.kaopiz.kprogresshud.KProgressHUD

import com.ciuwapp.prefs.PrefsManager

class LoginActivity : AppCompatActivity() {

    private lateinit var hud: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        et_email.setText("test@dev.com")
        et_password.setText("123456")

        btn_login.setOnClickListener {
            checkLogin()
        }

        btn_register.setOnClickListener {
            launchRegisterActivity()
        }
    }

    private fun checkLogin() {
        var email = et_email.text.toString()
        var password = et_password.text.toString()

        if(!AppHelper.isEmailValid(email)){
            et_email.error = "Enter the valid email."
            return
        }
        hud = KProgressHUD.create(this)
        hud.show()

        ClientAPIService.requestLogin(email, password) { succeeded, result ->
            hud.dismiss()
            if (succeeded) {
                val userData: UserData? = result
                if(userData?.token != null) {
                    AppHelper.userProfile = userData.user

                    loginWrapper.visibility = View.GONE
                    val imageView = ImageView(this)
                    imageView.setBackgroundResource(R.mipmap.ic_checkmark)
                    PrefsManager.newInstance(this)?.setEmail(email)
                    PrefsManager.newInstance(this)?.setPassword(password)
                    PrefsManager.newInstance(this)?.setToken(userData?.token)

                    hud.setCustomView(imageView)
                        .setLabel("Login Successful")
                        .show()
                    Handler().postDelayed({
                        hud.dismiss()
                        launchHomeActivity()
                    }, 2000)
                }
                else{
                    hud.setLabel("Failed to login")
                        .show()
                    Handler().postDelayed({
                        hud.dismiss()
                    }, 2000)
                }
            }
            else {
                hud.setLabel("Failed to login")
                    .show()
                Handler().postDelayed({
                    hud.dismiss()
                }, 2000)
            }
        }
    }
    private fun launchHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchRegisterActivity() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}
