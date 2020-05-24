package com.ciuwapp.activities

import com.ciuwapp.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import com.ciuwapp.data.UserData
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.helpers.AppHelper
import com.ciuwapp.prefs.PrefsManager
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {

    private lateinit var hud: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener {
            createNewAccount()
        }

        btn_back.setOnClickListener {
            launchLoginActivity()
        }
    }

    private fun createNewAccount() {
        var email = et_email.text.toString()
        var password = et_password.text.toString()
        var firstname = et_first_name.text.toString()
        var lastname = et_last_name.text.toString()

        if(!AppHelper.isEmailValid(email)) {
            //et_email.error = "Enter the valid email."
            this.launchAlertDialog("Enter the valid email.")
            return
        }
        if( password?.length < 6) {
//            et_password.error = "Password should be 6 in length at least."
            this.launchAlertDialog("Password should be 6 in length at least.")
            return
        }
        if(firstname?.isEmpty()){
//            et_first_name.error = "Enter your first name."
            this.launchAlertDialog("Enter your first name.")
            return
        }
        if(lastname?.isEmpty()){
//            et_last_name.error = "Enter your last name."
            this.launchAlertDialog("Enter your last name.")
            return
        }


        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.show()

        ClientAPIService.requestRegister(email, password, password, firstname, lastname) { succeeded, result ->
            hud.dismiss()
            if (succeeded) {
                val userData: UserData? = result
                if(userData?.token != null) {
                    AppHelper.userProfile = userData.user

                    PrefsManager.newInstance(this)?.setEmail(email)
                    PrefsManager.newInstance(this)?.setPassword(password)

                    hud.setLabel("Register Successful")
                        .show()
                    Handler().postDelayed({
                        hud.dismiss()
                        launchLoginActivity()
                    }, 2000)
                }
                else{
                    hud.setLabel("Failed to register")
                        .show()
                    Handler().postDelayed({
                        hud.dismiss()
                    }, 2000)
                }
            }
            else {
                hud.setLabel("Failed to register")
                    .show()
                Handler().postDelayed({
                    hud.dismiss()
                }, 2000)
            }
        }
    }

    private fun launchLoginActivity() {
        finish()
    }

    private fun launchAlertDialog(text: String) {

        var dlg = CustomAlertDialog(this)


        var clickListenerOk = View.OnClickListener {view ->
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
}
