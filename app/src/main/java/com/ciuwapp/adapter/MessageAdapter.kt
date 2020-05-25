package com.ciuwapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ciuwapp.R
import com.ciuwapp.model.MessageList
import java.util.*

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private lateinit var clickCallBack: (MessageList) -> Unit
    private val items: ArrayList<MessageList> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): MessageAdapter.MessageViewHolder {
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

    override fun onBindViewHolder(holder: MessageAdapter.MessageViewHolder, position: Int) {
        items[position].let {
            holder.month.text = items.get(position).month
            holder.day.text = items.get(position).day.toString()
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