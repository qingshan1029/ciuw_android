package com.ciuwapp.data

data class CalendarInfo (
    var id: Int,
    var title: String,
    var date: String,
    var start_time: String,
    var end_time: String,
    var location: String,
    var websiteurl: String,
    var description: String
)

data class CalendarData(
    var current_page: Int,
    var data: ArrayList<CalendarInfo>?,
    var first_page_url: String?,
    var from: Int,
    var next_page_url: String?,
    var path: String,
    var per_page: Int,
    var prev_page_url: String?,
    var to: Int
)
data class CalendarResponse(
    val events: CalendarData
)
