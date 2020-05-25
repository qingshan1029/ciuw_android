package com.ciuwapp.activities
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.adapter.CalendarAdapter
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.data.CalendarData
import com.ciuwapp.listener.EndlessRecyclerViewScrollListener
import com.ciuwapp.model.Calendar
import com.ciuwapp.prefs.PrefsManager
import kotlinx.android.synthetic.main.activity_calendar.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import kotlin.collections.ArrayList
import java.time.format.DateTimeFormatter

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
                requestNextCalendar()
            }
            override fun onRefresh() {
                requestFirstCalendar()
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

            onToastMessage(calendar.websiteurl)
        }

        requestFirstCalendar()
    }

    override fun onStart() {
        super.onStart()
    }

    private fun requestFirstCalendar() {
        if( loading )
            return
        showLoading()
        scrollListener.resetState()

        ClientAPIService.requestCalendar(PrefsManager.newInstance(this).getToken(), 0) { succeeded, result ->

            if (succeeded) {
                val msgData: CalendarData? = result
                current_Page = msgData?.current_page
                next_page_url = msgData?.next_page_url
                val calendarList: ArrayList<Calendar> = arrayListOf<Calendar>()

                for (item in msgData?.data!!) {
                    val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                    var sdf = SimpleDateFormat("HH:mm:ss")
                    var sdfs = SimpleDateFormat("hh:mm aa")

                    val start_time = sdf.parse(item.start_time)
                    val end_time = sdf.parse(item.end_time)

                    if( start_time != null && end_time != null )
                        calendarList.add(Calendar(date.month.toString(), date.dayOfMonth, sdfs.format(start_time), sdfs.format(end_time), item.title, item.location, item.websiteurl))
                }

                calendarAdapter.clear()
                calendarAdapter.setItems(calendarList)
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

    fun requestNextCalendar() {
        if( loading )
            return
        if( next_page_url == null )
            return

        showLoading()

        ClientAPIService.requestCalendar(PrefsManager.newInstance(this).getToken(), current_Page!!+1) { succeeded, result ->

            if (succeeded) {
                val msgData: CalendarData? = result
                current_Page = msgData?.current_page
                next_page_url = msgData?.next_page_url
                val calendarList: ArrayList<Calendar> = arrayListOf<Calendar>()

                for (item in msgData?.data!!) {
                    val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                    var start_time = LocalTime.parse(item.start_time, DateTimeFormatter.ofPattern("a"))

                    calendarList.add(Calendar(date.month.toString(), date.dayOfMonth, start_time.toString(), item.end_time, item.title, item.location, item.websiteurl))
                }
                calendarAdapter.setItems(calendarList)
                onToastMessage("page number ${current_Page}")
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
        progressBar1.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading = false
        progressBar1.visibility = View.GONE
    }

}
