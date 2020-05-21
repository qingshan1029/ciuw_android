package com.ciuwapp.data

data class Message (
    var id: Int,
    var date: String,
    var message: String
)

data class MessageData(
    var current_page: Int,
    var data: ArrayList<Message>?,
    var first_page_url: String?,
    var from: Int,
    var next_page_url: String?,
    var path: String,
    var per_page: Int,
    var prev_page_url: String?,
    var to: Int
)
data class MessageResponse(
    val messages: MessageData
)
