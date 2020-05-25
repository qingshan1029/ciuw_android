package com.ciuwapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.model.MessageList
import java.text.SimpleDateFormat
import java.util.*


class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private lateinit var clickCallBack: (MessageList) -> Unit
    private val items: ArrayList<MessageList> = ArrayList()
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_message_item, parent, false)
        val viewHolder = MessageViewHolder(view)
        view.setOnClickListener {
            //            clickListener.invoke(items[viewHolder.adapterPosition])
            clickCallBack(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val months = arrayOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC")
        val sdfymd = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.setTime(sdfymd.parse(items.get(position).date))

        items[position].let {

            holder.month.text = months.get(calendar.get(Calendar.MONTH))
            holder.day.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
            holder.content.text = items.get(position).content
        }
    }


    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val month = itemView.findViewById<TextView>(R.id.tv_message_month)
        val day = itemView.findViewById<TextView>(R.id.tv_message_day)
        val content = itemView.findViewById<TextView>(R.id.tv_message_content)
    }

    fun setItems(items: ArrayList<MessageList>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setItemClickListener(clickCallBack: (MessageList) -> Unit) {
        this.clickCallBack = clickCallBack
    }

    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }
}