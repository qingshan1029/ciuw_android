package com.ciuwapp.activities
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ciuwapp.R
import com.ciuwapp.adapter.CalendarAdapter
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.data.CalendarData
import com.ciuwapp.listener.OnLoadMoreListener
import com.ciuwapp.listener.RecyclerViewLoadMoreScroll
import com.ciuwapp.model.CalendarList
import com.ciuwapp.prefs.PrefsManager
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_calendar.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarActivity : AppCompatActivity() {

    private lateinit var hud: KProgressHUD
    private var current_Page: Int? = 0
    private var calendarList: ArrayList<CalendarList> = arrayListOf<CalendarList>()
    private var next_page_url: String? = null

    private var scrollListener: RecyclerViewLoadMoreScroll? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        id_calendar_back.setOnClickListener {
            launchHomeActivity()
        }
    }

    private fun launchHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.show()

        ClientAPIService.requestCalendar(PrefsManager.newInstance(this)?.getToken(), 0) { succeeded, result ->
            hud.dismiss()
            if (succeeded) {
                val msgData: CalendarData? = result
                current_Page = msgData?.current_page
                next_page_url = msgData?.next_page_url

                for (item in msgData?.data!!) {
                    val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                    calendarList.add(CalendarList(date.month.toString(), date.dayOfMonth, item.start_time, item.title, item.location))
                }

                rv_calendar.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                rv_calendar.setHasFixedSize(true)
                rv_calendar.adapter = CalendarAdapter(calendarList)

                scrollListener = RecyclerViewLoadMoreScroll(rv_calendar )
                scrollListener!!.setOnLoadMoreListener(object : OnLoadMoreListener {
                    override fun onLoadMore() {
                        loadMoreData()
                    }
                })

                rv_calendar!!.addOnScrollListener(scrollListener!!)

                scrollListener!!.setLoaded()
            }
            else {

            }

        }
    }


    private fun loadMoreData() {
        if( next_page_url == null )
            return
        hud = KProgressHUD.create(this)
        hud.setBackgroundColor(R.color.colorLoading)
        hud.show()

        ClientAPIService.requestCalendar(PrefsManager.newInstance(this)?.getToken(), current_Page!!+1) { succeeded, result ->
            hud.dismiss()
            if (succeeded) {
                val msgData: CalendarData? = result
                current_Page = msgData?.current_page
                next_page_url = msgData?.next_page_url

                for (item in msgData?.data!!) {
                    val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                    calendarList.add(CalendarList(date.month.toString(), date.dayOfMonth, item.start_time, item.title, item.location))
                }

                rv_calendar.adapter = CalendarAdapter(calendarList)

                scrollListener!!.setLoaded()
            }
            else {

            }
        }
    }
}
