package com.ciuwapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_message.*

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.adapter.MessageAdapter
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.data.*
import com.ciuwapp.listener.EndlessRecyclerViewScrollListener
import com.ciuwapp.model.MessageList
import com.ciuwapp.prefs.PrefsManager
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_message.tv_empty_messages
import kotlin.collections.ArrayList

class MessageActivity : AppCompatActivity() {
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
                requestMessage(current_Page!!)
            }
            override fun onRefresh() {
                requestMessage(0)
            }
        }
        rv_Messages.addOnScrollListener(scrollListener)
        rv_Messages.adapter = messageAdapter
    }

    private fun initListener() {
        // Adapter item clickListener
        messageAdapter.setItemClickListener {

        }

        requestMessage(0)
    }

    private fun requestMessage(pageNO: Int) {
        if( loading )
            return

        if( pageNO != 0 && next_page_url == null )
            return

        showLoading()
        scrollListener.resetState()


        ClientAPIService.requestMessage(PrefsManager.newInstance(this).getToken(), pageNO+1) { succeeded, result ->
            if (succeeded) {
                val msgData: MessageData? = result
                current_Page = msgData?.current_page
                next_page_url = msgData?.next_page_url
                val messageList: ArrayList<MessageList> = arrayListOf()
                for (item in msgData?.data!!) {
                    messageList.add(MessageList(item.date, item.message))
                }

                if( pageNO == 0 )
                    messageAdapter.clear()
                messageAdapter.setItems(messageList)
                messageAdapter.notifyDataSetChanged()
                scrollListener.setLoaded()

                if( messageAdapter.itemCount < 1 )
                    tv_empty_messages.visibility = View.VISIBLE
                else
                    tv_empty_messages.visibility = View.GONE

                hideLoading()
            }
            else {
                onToastMessage("Network Error")
                hideLoading()
            }

        }
    }

    private fun onToastMessage(message: String) {
//        Log.d("message status", message)
    }

    private fun showLoading() {
        loading = true
        progressBar2.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading = false
        progressBar2.visibility = View.GONE
    }
}
