package com.ciuwapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_message.*

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.adapter.MessageAdapter
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.data.*
import com.ciuwapp.listener.EndlessRecyclerViewScrollListener
import com.ciuwapp.model.MessageList
import com.ciuwapp.prefs.PrefsManager
import com.kaopiz.kprogresshud.KProgressHUD
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.ArrayList

class MessageActivity : AppCompatActivity() {
    private lateinit var hud: KProgressHUD
    private var current_Page: Int? = 0
    private var next_page_url: String? = null

    private lateinit var rv_Messages: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private var loading : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        id_message_back.setOnClickListener {
            launchHomeActivity()
        }

        initView()
        initAdapter()
        initListener()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun launchHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initView() {
        rv_Messages = findViewById(R.id.rv_message)
    }

    private fun initAdapter() {
        messageAdapter = MessageAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        rv_Messages.layoutManager = linearLayoutManager
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore1(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                requestNextMessage()
            }
            override fun onRefresh() {
                requestFirstMessage()
            }
        }
        rv_Messages.addOnScrollListener(scrollListener)
        rv_Messages.adapter = messageAdapter
    }

    private fun initListener() {
        // Adapter item clickListener
        messageAdapter.setItemClickListener { message ->
//            Intent(Intent.ACTION_VIEW, Uri.parse(message.content)).takeIf {
//                it.resolveActivity(packageManager) != null
//            }?.run(this::startActivity)
//
//            Toast.makeText(this, message.content, Toast.LENGTH_SHORT).show()
        }

        requestFirstMessage()
    }

    private fun requestFirstMessage() {
        if( loading )
            return
        showLoading()
        scrollListener.resetState()

        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.show()

        ClientAPIService.requestMessage(PrefsManager.newInstance(this)?.getToken(), 0) { succeeded, result ->
            hud.dismiss()
            if (succeeded) {
                val msgData: MessageData? = result
                current_Page = msgData?.current_page
                next_page_url = msgData?.next_page_url
                val messageList: ArrayList<MessageList> = arrayListOf<MessageList>()
                for (item in msgData?.data!!) {
                    val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                    messageList.add(MessageList(date.month.toString(), date.dayOfMonth, item.message))
                }

                messageAdapter.clear()
                messageAdapter.setItems(messageList)
                onToastMessage("success adapter initialization and loading message data")
                scrollListener.setLoaded()
                hideLoading()
            }
            else {
                onToastMessage("Network Error")
                hideLoading()
            }

        }
    }

    fun requestNextMessage() {
        if( loading )
            return
        if( next_page_url == null )
            return

        showLoading()

        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.show()

        ClientAPIService.requestMessage(PrefsManager.newInstance(this)?.getToken(), current_Page!!+1) { succeeded, result ->
            hud.dismiss()

            if (succeeded) {
                val msgData: MessageData? = result

                if( msgData?.data  == null || msgData?.data.size < 1)
                    onToastMessage("there is no exist no longer")
                else {
                    current_Page = msgData?.current_page
                    next_page_url = msgData?.next_page_url
                    val messageList: ArrayList<MessageList> = arrayListOf<MessageList>()

                    for (item in msgData?.data!!) {
                        val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                        messageList.add(MessageList(date.month.toString(), date.dayOfMonth, item.message))
                    }

                    messageAdapter.setItems(messageList)
                    onToastMessage("page number ${current_Page}")
                }
                scrollListener.setLoaded()
                hideLoading()
            }
            else {
                onToastMessage("Network Error")
                hideLoading()
            }
        }
    }

    private fun onToastMessage(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.d("message status", message)
    }

    private fun showLoading() {
        loading = true
//        pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading = false
//        pbLoading.visibility = View.GONE
    }

}
