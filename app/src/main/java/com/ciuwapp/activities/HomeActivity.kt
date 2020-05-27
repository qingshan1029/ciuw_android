package com.ciuwapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import com.ciuwapp.R
import com.ciuwapp.prefs.PrefsManager
import kotlinx.android.synthetic.main.activity_home.*
import com.ciuwapp.api.ClientAPIService
import com.google.firebase.messaging.FirebaseMessaging

class HomeActivity : AppCompatActivity() {

    private var webURL : String = ""
    private var webTitle: String = ""


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
            webTitle = "BENEFITS"
            launchWebViewActivity()
        }

        id_home_contract.setOnClickListener {
            webURL = "contents/contract"
            webTitle = "CONTRACT"

            launchWebViewActivity()
        }

        id_home_meeting.setOnClickListener {
            webURL = "contents/meeting"
            webTitle = "MEETING"
            launchWebViewActivity()
        }

        id_home_contact.setOnClickListener {
            webURL = "contents/contact"
            webTitle = "CONTACT"
            launchWebViewActivity()
        }
        FirebaseMessaging.getInstance().subscribeToTopic("general")
    }

    private fun launchAlertDialog() {

        var dlg = CustomAlertDialog(this)

        var clickListenerYes = View.OnClickListener {

            dlg.dismiss()
            PrefsManager.newInstance(this).logout()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        var clickListenerNo = View.OnClickListener {
            dlg.dismiss()
        }

        dlg.addButton("Yes", clickListenerYes)
//        dlg.addButton("No", clickListenerNo)
        dlg.setIcon(R.drawable.ic_question_mark)
        dlg.setConfirmButtonListener(clickListenerNo)
        dlg.setConfirmText("No")
        dlg.confirmButtonColor(R.color.colorPrimary)
        dlg.setTitleText("Logout?")
        dlg.setContentText("Press Yes to logout")

        dlg.show()

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
        intent.putExtra("webTitle", webTitle)
        startActivity(intent)
    }

}
