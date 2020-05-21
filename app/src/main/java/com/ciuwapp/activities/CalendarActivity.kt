package com.ciuwapp.activities
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.adapter.CalendarAdapter
import com.ciuwapp.api.ClientAPIService
import com.ciuwapp.data.CalendarData
import com.ciuwapp.model.CalendarList
import com.ciuwapp.prefs.PrefsManager
import com.kaopiz.kprogresshud.KProgressHUD
import kotlinx.android.synthetic.main.activity_calendar.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarActivity : AppCompatActivity() {

    private lateinit var hud: KProgressHUD
    private var currentPage: Int? = 0
    private var calendarList: ArrayList<CalendarList> = arrayListOf<CalendarList>()

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
        loadCalendarList()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun loadCalendarList() {

        hud = KProgressHUD.create(this)
        hud.show()

        ClientAPIService.requestCalendar(PrefsManager.newInstance(this)?.getToken()) { succeeded, result ->
            hud.dismiss()
            if (succeeded) {
                val msgData: CalendarData? = result
                currentPage = msgData?.current_page

                for (item in msgData?.data!!) {
                    val date = LocalDate.parse(item.date, DateTimeFormatter.ISO_DATE)
                    calendarList.add(CalendarList(date.month.toString(), date.dayOfMonth, item.start_time, item.title, item.location))
                }

                rv_calendar.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                rv_calendar.setHasFixedSize(true)
                rv_calendar.adapter = CalendarAdapter(calendarList)

//                rv_message.addOnScrollListener()
            }
            else {

            }
        }
    }
}
