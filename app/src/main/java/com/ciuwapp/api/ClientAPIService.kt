package com.ciuwapp.api
import android.util.Log
import com.ciuwapp.data.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import retrofit2.converter.gson.GsonConverterFactory

class ClientAPIService {
    companion object {
        val hostURL = "https://dev5.pinpointdigital.com/"
        val baseURL = "https://dev5.pinpointdigital.com/api/"
        val client: ClientLoginAPIClient = ClientLoginAPIClient.create()

        fun requestLogin(
            email: String, password: String, callback: (succeeded: Int, result: UserData?) -> Unit
        ) {
            client.requestLogin(email,password).enqueue(object: Callback<UserData>{

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.d(
                        "ClientAPIService",
                        "Could not get network status ${t.localizedMessage}"
                    )
                    callback(-1, null)
                }
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    Log.d("ClientAPIService", "Got the user back ${response.body()}")
                    val code = response.code()
                    if (code == 200 && response.body() != null) {
                        var postResponse = response.body()!!
                        callback(code, postResponse)
                    } else if( code == 400 ) {
                        callback(code, null)
                    } else if( code == 401 ) {
                        callback(code, null)
                    } else if( code == 200 ) {  // Ok but body is null
                        callback(402, null)
                    } else {
                        callback(404, null)
                    }
                }
            })
        }

        fun requestRegister(
            email: String, password: String, password_confirmation: String, first_name: String, last_name: String, callback: (succeeded: Int, result: UserData?) -> Unit
        ) {
            client.requestRegister(email,password,password_confirmation,first_name,last_name).enqueue(object: Callback<UserData>{

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.d(
                        "ClientAPIService",
                        "Could not get network status ${t.localizedMessage}"
                    )
                    callback(-1, null)
                }
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    Log.d("ClientAPIService", "Got the user back ${response.body()}")
                    val code = response.code()
                    if (code == 201) {
                        var postResponse = response.body()!!
                        callback(code, postResponse)
                    } else if( code == 400 ) {
                        callback(code, null)
                    }
                    else {
                        callback(404, null)
                    }
                }
            })
        }

        fun requestForgotPassword(
            email: String, password: String, callback: (succeeded: Int, result: UserData?) -> Unit
        ) {
            client.requestForgotPassword(email,password).enqueue(object: Callback<UserData>{

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.d(
                        "ClientAPIService",
                        "Error forgot password ${t.localizedMessage}"
                    )
                    callback(-1, null)
                }
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    Log.d("ClientAPIService", "Got feedback about sending your email")
                     callback(response.code(), null)
                }
            })
        }

        fun requestResetPassword(
            email: String, verify_code: String, callback: (succeeded: Int, result: UserData?) -> Unit
        ) {
            client.requestResetPassword(email,verify_code).enqueue(object: Callback<UserData>{

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.d(
                        "ClientAPIService",
                        "Error verify code"
                    )
                    callback(-1, null)
                }
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    Log.d("ClientAPIService", "Got feedback about sending verify code")
                    callback(response.code(), null)
                }
            })
        }

        fun requestMessage(
            token: String, pageNO: Int, callback: (succeeded: Boolean, result: MessageData?) -> Unit
        ) {
            client.requestMessage("Bearer $token", pageNO).enqueue(object: Callback<MessageResponse>{

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.d(
                        "ClientAPIService",
                        "Could not get network status ${t.localizedMessage}"
                    )
                    callback(false, null)
                }
                override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                    Log.d("ClientAPIService-TOKEN", " ${response}")
                    Log.d("ClientAPIService", "Got the message data ${response.body()}")
                    if (response.body() != null) {
                        var postResponse = response.body()!!
                        callback(true, postResponse.messages)
                    } else {
                        callback(false, null)
                    }
                }
            })
        }
        fun requestCalendar(
            token: String, pageNO: Int,callback: (succeeded: Boolean, result: CalendarData?) -> Unit
        ) {
            client.requestCalendar("Bearer $token", pageNO).enqueue(object: Callback<CalendarResponse>{

                override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                    Log.d(
                        "ClientAPIService",
                        "Could not get network status ${t.localizedMessage}"
                    )
                    callback(false, null)
                }
                override fun onResponse(call: Call<CalendarResponse>, response: Response<CalendarResponse>) {
                    Log.d("ClientAPIService-TOKEN", " ${response}")
                    Log.d("ClientAPIService", "Got the calendar data ${response.body()}")
                    if (response.body() != null) {
                        var postResponse = response.body()!!
                        callback(true, postResponse.events)
                    } else {
                        callback(false, null)
                    }
                }
            })
        }

    }
}

interface ClientLoginAPIClient{
    @POST("login")
    @FormUrlEncoded
    fun requestLogin(
        @Field("email") email:String,
        @Field("password") password:String
    ) : Call<UserData>

    @POST("register")
    @FormUrlEncoded
    fun requestRegister(
        @Field("email") email:String,
        @Field("password") password:String,
        @Field("password_confirmation") password_confirmation:String,
        @Field("firstname") first_name:String,
        @Field("lastname") last_name:String
        ) : Call<UserData>

    @POST("forgotpassword")
    @FormUrlEncoded
    fun requestForgotPassword(
        @Field("email") email:String,
        @Field("password") password:String
    ) : Call<UserData>

    @POST("resetpassword")
    @FormUrlEncoded
    fun requestResetPassword(
        @Field("email") email:String,
        @Field("verify_code") verify_code:String
    ) : Call<UserData>

    @POST("messages")
    fun requestMessage(
        @Header("Authorization") token: String,
        @Query("page") pageNO: Int
    ): Call<MessageResponse>


    @POST("events")
    fun requestCalendar(
        @Header("Authorization") token: String,
        @Query("page") pageNO: Int
        ): Call<CalendarResponse>


    companion object Factory {
        fun create(): ClientLoginAPIClient {
            Log.d("Client Portal Service", "Create Factory")
            val retrofit = Retrofit.Builder()
                .baseUrl(ClientAPIService.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ClientLoginAPIClient::class.java)
        }
    }
}

