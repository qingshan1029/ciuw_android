package com.ciuwapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.model.Calendar
import java.util.ArrayList

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private lateinit var clickCallBack: (Calendar) -> Unit
    private val items: ArrayList<Calendar> = ArrayList()

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
//      return items.size
        return if (items == null) 0 else items!!.size
    }

    override fun onBindViewHolder(holder: CalendarAdapter.CalendarViewHolder, position: Int) {
//        holder.gendar.setImageResource(calendarList.get(position).gendar)
        holder.month.text = items.get(position).month
        holder.day.text = items.get(position).day.toString()
        holder.start_time.text = items.get(position).start_time
        holder.end_time.text = items.get(position).end_time
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

    fun setItems(items: ArrayList<Calendar>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setItemClickListener(clickCallBack: (Calendar) -> Unit) {
        this.clickCallBack = clickCallBack
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}