package com.ciuwapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.ciuwapp.R
import com.ciuwapp.prefs.PrefsManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_dialog.view.*
import androidx.appcompat.app.AlertDialog
import com.ciuwapp.api.ClientAPIService

class HomeActivity : AppCompatActivity() {

    private var webURL : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        id_logout.setOnClickListener {
            launchAlertDialog()
        }

        id_home_calendar.setOnClickListener {
            launchCalendarActivity()
        }

        id_home_message.setOnClickListener {
            launchMessageActivity()
        }

        id_home_benefits.setOnClickListener {
            webURL = "contents/benefits"
            launchWebViewActivity()
        }

        id_home_contract.setOnClickListener {
            webURL = "contents/contract"
            launchWebViewActivity()
        }

        id_home_meeting.setOnClickListener {
            webURL = "contents/meeting"
            launchWebViewActivity()
        }

        id_home_contact.setOnClickListener {
            webURL = "contents/contact"
            launchWebViewActivity()
        }
    }

    private fun launchAlertDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog, null)
        //AlertDialogBuilder
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("")



        mDialogView.dialogTitle.setText("Notice")
        mDialogView.dialogContent.setText("Will you sure logout?")

        //show dialog
        val mAlertDialog = mBuilder.show()

        //login button click of custom layout
        mDialogView.dialogBtnYes.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
            //get text from EditTexts of custom layout
            val name = mDialogView.dialogTitle.text.toString()
            val email = mDialogView.dialogContent.text.toString()

            PrefsManager.newInstance(this).logout()

            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        //cancel button click of custom layout
        mDialogView.dialogBtnNo.setOnClickListener {
            //dismiss dialog
            mAlertDialog.dismiss()
        }
    }

    private fun launchCalendarActivity() {
        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchMessageActivity() {
        val intent = Intent(this, MessageActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun launchWebViewActivity() {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", "${ClientAPIService.hostURL}${webURL}")
        startActivity(intent)
    }

}
