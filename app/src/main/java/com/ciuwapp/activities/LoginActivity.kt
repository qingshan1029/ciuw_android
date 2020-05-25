package com.ciuwapp.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast

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

        btn_login.setOnClickListener {
            launchLogin()
        }

        btn_register.setOnClickListener {
            launchRegisterActivity()
        }

        tv_pw_forgot.setOnClickListener {
            launchForgotPassword()
        }
    }

    private fun launchLogin() {
        var email = et_email.text.toString()
        var password = et_password.text.toString()

        if( email.isEmpty() ) {
            this.launchAlertDialog("Enter your email.")
            return
        }

        if(!AppHelper.isEmailValid(email)){
            this.launchAlertDialog("Enter the valid email.")
            return
        }

        if( password.isEmpty() ) {
            this.launchAlertDialog("Enter your password.")
            return
        }

        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.setLabel("Logging in...")
        hud.show()

        ClientAPIService.requestLogin(email, password) { succeeded, result ->
            hud.dismiss()
            if (succeeded == 200) {
                val userData: UserData? = result
                if(userData?.token != null) {
                    AppHelper.userProfile = userData.user

                    PrefsManager.newInstance(this).setEmail(email)
                    PrefsManager.newInstance(this).setPassword(password)
                    PrefsManager.newInstance(this).setToken(userData.token)

                    hud.setLabel("Login successful.")
                        .show()
                    Handler().postDelayed({
                        hud.dismiss()
                        launchHomeActivity()
                    }, 2000)
                }
                else{
                    hud.setLabel("Failed to login.") //token is null
                        .show()
                    Handler().postDelayed({
                        hud.dismiss()
                    }, 2000)
                }
            } else if (succeeded == 401) {// error: Your are not approved yet
                hud.setLabel("Not approved yet.")
                    .show()
                Handler().postDelayed({
                    hud.dismiss()
                }, 2000)
            }
            else if( succeeded == 400 ) {  // error: invalid user name or password
                hud.setLabel("Invalid email or password.")
                    .show()
                Handler().postDelayed({
                    hud.dismiss()
                }, 2000)
            }
            else {
                hud.setLabel("Failed to login.")
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
    private fun launchAlertDialog(text: String) {

        var dlg = CustomAlertDialog(this)


        var clickListenerOk = View.OnClickListener {
            dlg.dismiss()
        }

        dlg.setIcon(R.drawable.ic_close_black_24dp)
        dlg.setConfirmButtonListener(clickListenerOk)
        dlg.setConfirmText("OK")
        dlg.confirmButtonColor(R.color.colorPrimary)
        dlg.setTitleText("CIUW")
        dlg.setContentText(text)

        dlg.show()

    }
    private fun launchForgotPassword() {
        val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }
}
