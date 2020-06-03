package com.ciuwapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import com.ciuwapp.R
import com.ciuwapp.prefs.PrefsManager
import kotlinx.android.synthetic.main.activity_home.*
import com.ciuwapp.api.ClientAPIService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
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

        id_home_miscellaneous.setOnClickListener {
            webURL = "contents/miscellaneous"
            webTitle = "Miscellaneous"
            launchWebViewActivity()
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")

        registerPhone()
    }

    private fun unregisterPhone() {
        val email = PrefsManager.newInstance(this).getEmail()
        val fcmToken = PrefsManager.newInstance(this).getFCMToken()
        val token = PrefsManager.newInstance(this).getToken()
        ClientAPIService.requestPhoneUnregister(token, email, fcmToken) { succeeded, result ->
            if (succeeded == 200)
                Log.d("fcm token unregister", "successful")
            else
                Log.d("fcm token unregister", "failed")
        }
    }

    private fun registerPhone() {
        val logTag = String::class.java.simpleName
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d(logTag, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val fcmToken = task.result?.token
                Log.d("getInstanceId failed", fcmToken)
                PrefsManager.newInstance(this).setFCMToken(fcmToken!!)

                val email = PrefsManager.newInstance(this).getEmail()
                val token = PrefsManager.newInstance(this).getToken()

                ClientAPIService.requestPhoneRegister(token, email, fcmToken) { succeeded, result ->
                    if (succeeded == 200)
                        Log.d("fcm token registration", "successful")
                    else
                        Log.d("fcm token registration", "failed")
                }
            })
    }

    private fun launchAlertDialog() {

        val dlg = CustomAlertDialog(this)

        val clickListenerYes = View.OnClickListener {

            unregisterPhone()

            dlg.dismiss()

            PrefsManager.newInstance(this).logout()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            finish()
            startActivity(intent)
        }

        val clickListenerNo = View.OnClickListener {
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
        //finish()
    }

    private fun launchMessageActivity() {
        val intent = Intent(this, MessageActivity::class.java)
        startActivity(intent)
        //finish()
    }

    private fun launchWebViewActivity() {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", "${ClientAPIService.hostURL}${webURL}")
        intent.putExtra("webTitle", webTitle)
        startActivity(intent)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

}
