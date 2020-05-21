package com.ciuwapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.model.CalendarList

class CalendarAdapter (val calendarList: ArrayList<CalendarList>) : RecyclerView.Adapter<CalendarAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): CalendarAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_calendar_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return calendarList.size
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CustomViewHolder, position: Int) {
//        holder.gendar.setImageResource(calendarList.get(position).gendar)
        holder.month.text = calendarList.get(position).month
        holder.day.text = calendarList.get(position).day.toString()
        holder.time.text = calendarList.get(position).time
        holder.content.text = calendarList.get(position).content
        holder.address.text = calendarList.get(position).address
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val month = itemView.findViewById<TextView>(R.id.tv_calendar_month)
        val day = itemView.findViewById<TextView>(R.id.tv_calendar_day)
        val time = itemView.findViewById<TextView>(R.id.tv_calendar_time)
        val content = itemView.findViewById<TextView>(R.id.tv_calendar_content)
        val address = itemView.findViewById<TextView>(R.id.tv_calendar_address)

    }

}