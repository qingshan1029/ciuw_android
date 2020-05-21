package com.ciuwapp.activities

import android.annotation.TargetApi
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_message.*

import android.content.Intent
import android.os.Build
import androidx.recyclerview.widget.LinearLayoutManager
import com.ciuwapp.R
import com.ciuwapp.adapter.MessageAdapter
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.data.*
import com.ciuwapp.model.MessageList
import com.ciuwapp.prefs.PrefsManager
import com.kaopiz.kprogresshud.KProgressHUD
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class MessageActivity : AppCompatActivity() {
    private lateinit var hud: KProgressHUD
    private var currentPage: Int? = 0
    private var messageList: ArrayList<MessageList> = arrayListOf<MessageList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        id_message_back.setOnClickListener {
            launchHomeActivity()
        }
    }

    override fun onStart() {
        super.onStart()
        loadMessageList()
    }
    private fun launchHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun loadMessageList() {

        hud = KProgressHUD.create(this)
        hud.show()

        ClientAPIService.requestMessage(PrefsManager.newInstance(this)?.getToken()) { succeeded, result ->
            hud.dismiss()
            if (succeeded) {
                val msgData: MessageData? = result
                currentPage = msgData?.current_page

                for (item in msgData?.data!!) {
                    val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                    messageList.add(MessageList(date.month.toString(), date.dayOfMonth, item.message))
                }

                rv_message.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                rv_message.setHasFixedSize(true)
                rv_message.adapter = MessageAdapter(messageList)
//                rv_message.addOnScrollListener()
            }
            else {

            }
        }
    }

}
