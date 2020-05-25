package com.ciuwapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.model.CalendarList
import java.text.SimpleDateFormat
import java.util.*




class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private lateinit var clickCallBack: (CalendarList) -> Unit
    private val items: ArrayList<CalendarList> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): CalendarAdapter.CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_calendar_item, parent, false)
        val viewHolder = CalendarViewHolder(view)
        view.setOnClickListener {
//          clickListener.invoke(items[viewHolder.adapterPosition])
            clickCallBack(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CalendarViewHolder, position: Int) {
        val months = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
        val sdf = SimpleDateFormat("HH:mm:ss")
        val sdfs = SimpleDateFormat("hh:mm aa")
        val sdfymd = SimpleDateFormat("yyyy-MM-dd")

        val calendar = Calendar.getInstance()
        calendar.setTime(sdfymd.parse(items.get(position).date))

        holder.month.text = months.get(calendar.get(Calendar.MONTH))
        holder.day.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        holder.start_time.text = sdfs.format(sdf.parse(items.get(position).start_time)).toString()
        holder.end_time.text = sdfs.format(sdf.parse(items.get(position).end_time)).toString()
        holder.content.text = items.get(position).content
        holder.address.text = items.get(position).address
    }


    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val month = itemView.findViewById<TextView>(R.id.tv_calendar_month)
        val day = itemView.findViewById<TextView>(R.id.tv_calendar_day)
        val start_time = itemView.findViewById<TextView>(R.id.tv_calendar_start_time)
        val end_time = itemView.findViewById<TextView>(R.id.tv_calendar_end_time)
        val content = itemView.findViewById<TextView>(R.id.tv_calendar_content)
        val address = itemView.findViewById<TextView>(R.id.tv_calendar_address)
    }

    fun setItems(items: ArrayList<CalendarList>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setItemClickListener(clickCallBack: (CalendarList) -> Unit) {
        this.clickCallBack = clickCallBack
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}