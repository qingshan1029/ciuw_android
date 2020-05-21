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

class HomeActivity : AppCompatActivity() {

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
            launchCalendarActivity()
        }

        id_home_contract.setOnClickListener {
            launchCalendarActivity()
        }

        id_home_meeting.setOnClickListener {
            launchCalendarActivity()
        }

        id_home_contact.setOnClickListener {
            launchCalendarActivity()
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
}
