package com.ciuwapp.data

data class UserProfile (
    var id: Int,
    var firstname: String,
    var lastname: String,
    var email: String,
    var email_verified_at: String,
    var avatar: String,
    var role: Int,
    var updated_at: String,
    var created_at: String
)

data class UserData (
    var token: String,
    var user: UserProfile
)

data class ErrorData (
    var error: String
)
