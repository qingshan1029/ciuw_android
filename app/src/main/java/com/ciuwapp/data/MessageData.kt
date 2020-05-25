package com.ciuwapp.data

data class MessageInfo (
    val id: Int,
    val date: String,
    val message: String
)

data class MessageData(
    val current_page: Int,
    val data: ArrayList<MessageInfo>?,
    val first_page_url: String?,
    val from: Int,
    val next_page_url: String?,
    val path: String,
    val per_page: Int,
    val prev_page_url: String?,
    val to: Int
)
data class MessageResponse(
    val messages: MessageData
)
