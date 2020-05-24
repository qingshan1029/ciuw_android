package com.ciuwapp.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ciuwapp.R
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.helpers.AppHelper
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.et_email
import kotlinx.android.synthetic.main.activity_forgot_password.et_password
import java.util.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var hud: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btn_reset_password_back.setOnClickListener {
            launchLoginActivity()
        }

        btn_reset_password.setOnClickListener {
            launchResetPassword()
        }
    }

    private fun launchResetPassword() {
        var email = et_email.text.toString()
        var password = et_password.text.toString()


        if(!AppHelper.isEmailValid(email)){
            this.launchAlertDialog("Enter the valid email.")
            return
        }

        if( password.length < 6 ) {
            this.launchAlertDialog("Password should be 6 in length at least.")
            return
        }

        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.show()

        ClientAPIService.requestForgotPassword(email, password) { succeeded, result ->
            hud.dismiss()
            if (succeeded == 200) {
                launchVerifyCodeActivity()
            } else if (succeeded == 400) {  // error:
                hud.setLabel("The email unregistered")
                    .show()
                Handler().postDelayed({
                    hud.dismiss()
                }, 2000)
            } else {
                hud.setLabel("Failed")
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

    private fun launchVerifyCodeActivity() {
        var dlg = VerifyCodeActivity(this)

        var clickListenerYes = View.OnClickListener { view ->

            sendVerification(dlg, dlg.editText?.text.toString())
        }

        var clickListenerNo = View.OnClickListener {view ->
            dlg.dismiss()
        }

        dlg.setOkClickListener(clickListenerYes)
        dlg.setCancelClickListener(clickListenerNo)
        dlg.setTitleText("Verification code is sent to your email.")

        dlg.show()
    }

    private fun launchFinish() {
        finish()
    }

    private fun sendVerification(dlg: VerifyCodeActivity, code: String) {
        var email = et_email.text.toString()

        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.show()

        ClientAPIService.requestResetPassword(email, code) { succeeded, result ->
            hud.dismiss()

            if (succeeded == 200) {
                hud.setLabel("successful")
                    .show()
                Handler().postDelayed({
                    hud.dismiss()
                    dlg.dismiss()
                    finish()
                }, 2000)

            } else if (succeeded == 401) {// error:
                hud.setLabel("incorrect code")
                    .show()
                Handler().postDelayed({
                    hud.dismiss()
                }, 2000)
            } else {
                hud.setLabel("Failed")
                    .show()
                Handler().postDelayed({
                    hud.dismiss()
                }, 2000)
            }
        }
    }
    private fun launchVerifyAlert() {
        val verifyDialog = AlertDialog.Builder(this)
        val myview: View = layoutInflater.inflate(R.layout.verify_code, null)

        verifyDialog.setView(myview)

        val btn_send: Button = myview.findViewById(R.id.btn_send)
        val btn_verify_cancel: Button = myview.findViewById(R.id.btn_verify_cancel)
        val text_code: EditText = myview.findViewById(R.id.et_verify_code)

        btn_send.setOnClickListener {

        }

        btn_verify_cancel.setOnClickListener {

        }

        verifyDialog.show().window?.setBackgroundDrawableResource(R.drawable.round_style)
    }
}
