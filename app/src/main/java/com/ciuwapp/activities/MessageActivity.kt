package com.ciuwapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_message.*

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.ciuwapp.R
import com.ciuwapp.adapter.MessageAdapter
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.data.*
import com.ciuwapp.listener.OnLoadMoreListener
import com.ciuwapp.listener.RecyclerViewLoadMoreScroll
import com.ciuwapp.model.MessageList
import com.ciuwapp.prefs.PrefsManager
import com.kaopiz.kprogresshud.KProgressHUD
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class MessageActivity : AppCompatActivity() {
    private lateinit var hud: KProgressHUD
    private var current_Page: Int? = 0
    private val messageList: ArrayList<MessageList> = arrayListOf<MessageList>()
    private var next_page_url: String? = null

    private var scrollListener: RecyclerViewLoadMoreScroll? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        id_message_back.setOnClickListener {
            launchHomeActivity()
        }
    }

    override fun onStart() {
        super.onStart()

        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.show()

        ClientAPIService.requestMessage(PrefsManager.newInstance(this)?.getToken(), 0) { succeeded, result ->
            hud.dismiss()
            if (succeeded) {
                val msgData: MessageData? = result
                current_Page = msgData?.current_page
                next_page_url = msgData?.next_page_url

                for (item in msgData?.data!!) {
                    val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                    messageList.add(MessageList(date.month.toString(), date.dayOfMonth, item.message))
                }

                rv_message.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                rv_message.setHasFixedSize(true)
                rv_message.adapter = MessageAdapter(messageList)

                scrollListener = RecyclerViewLoadMoreScroll(rv_message )
                scrollListener!!.setOnLoadMoreListener(object : OnLoadMoreListener {
                    override fun onLoadMore() {
                        loadMoreData()
                    }
                })

                rv_message!!.addOnScrollListener(scrollListener!!)

                scrollListener!!.setLoaded()
            }
            else {

            }
        }
    }

    private fun launchHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadMoreData() {
        if( next_page_url == null )
            return

        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.show()

        ClientAPIService.requestMessage(PrefsManager.newInstance(this)?.getToken(), current_Page!!+1 ) { succeeded, result ->
            hud.dismiss()
            if (succeeded) {
                val msgData: MessageData? = result
                current_Page = msgData?.current_page
                next_page_url = msgData?.next_page_url

                for (item in msgData?.data!!) {
                    val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                    messageList.add(MessageList(date.month.toString(), date.dayOfMonth, item.message))
                }

                rv_message.adapter = MessageAdapter(messageList)
            }
            else {

            }
            scrollListener!!.setLoaded()
        }
    }
}
