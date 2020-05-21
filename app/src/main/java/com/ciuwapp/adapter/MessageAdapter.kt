package com.ciuwapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.model.MessageList

class MessageAdapter (val messageList: ArrayList<MessageList>) : RecyclerView.Adapter<MessageAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): MessageAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_message_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageAdapter.CustomViewHolder, position: Int) {
        holder.month.text = messageList.get(position).month
        holder.day.text = messageList.get(position).day.toString()
        holder.content.text = messageList.get(position).content
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val month = itemView.findViewById<TextView>(R.id.tv_message_month)
        val day = itemView.findViewById<TextView>(R.id.tv_message_day)
        val content = itemView.findViewById<TextView>(R.id.tv_message_content)
    }

}