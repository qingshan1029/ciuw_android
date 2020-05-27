package com.ciuwapp.activities
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.adapter.CalendarAdapter
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.data.CalendarData
import com.ciuwapp.listener.EndlessRecyclerViewScrollListener
import com.ciuwapp.model.CalendarList
import com.ciuwapp.prefs.PrefsManager
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlin.collections.ArrayList

class CalendarActivity : AppCompatActivity() {

    private var current_Page: Int? = 0
    private var next_page_url: String? = null

    private lateinit var rv_calendar: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener


    private var loading : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        initView()
        initAdapter()
        initListener()

        id_calendar_back.setOnClickListener {
            launchHomeActivity()
        }
    }

    private fun launchHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initView() {
        rv_calendar = findViewById(R.id.rv_calendar)
    }

    private fun initAdapter() {
        calendarAdapter = CalendarAdapter()
        val linearLayoutManager = LinearLayoutManager(this)
        rv_calendar.layoutManager = linearLayoutManager
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore1(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                requestCalendar(current_Page!!)
            }
            override fun onRefresh() {
                requestCalendar(0)
            }
        }
        rv_calendar.addOnScrollListener(scrollListener)
        rv_calendar.adapter = calendarAdapter
    }

    private fun initListener() {
        // Adapter item clickListener
        calendarAdapter.setItemClickListener { calendar ->
            Intent(Intent.ACTION_VIEW, Uri.parse(calendar.websiteurl)).takeIf {
                it.resolveActivity(packageManager) != null
            }?.run(this::startActivity)

            onToastMessage(calendar.websiteurl!!)
        }

        requestCalendar(0)
    }

    private fun requestCalendar(pageNO: Int) {
        if( loading )
            return

        if( pageNO != 0 && next_page_url == null )
            return

        showLoading()
        scrollListener.resetState()


        ClientAPIService.requestCalendar(PrefsManager.newInstance(this).getToken(), pageNO+1) { succeeded, result ->

            if (succeeded) {
                val msgData: CalendarData? = result
                current_Page = msgData?.current_page
                next_page_url = msgData?.next_page_url
                val calendarList: ArrayList<CalendarList> = arrayListOf()
                for (item in msgData?.data!!) {
                    calendarList.add(CalendarList(item.date, item.start_time, item.end_time, item.title, item.location, item.websiteurl))
                }

                if( pageNO == 0 )
                    calendarAdapter.clear()
                calendarAdapter.setItems(calendarList)
                scrollListener.setLoaded()

                if( calendarAdapter.itemCount < 1 )
                    tv_empty_events.visibility = View.VISIBLE
                else
                    tv_empty_events.visibility = View.GONE
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
        progressBar1.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading = false
        progressBar1.visibility = View.GONE
    }

}
