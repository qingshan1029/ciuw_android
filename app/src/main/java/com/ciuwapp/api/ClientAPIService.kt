package com.ciuwapp.api
import android.util.Log

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import com.ciuwapp.data.UserData
import retrofit2.converter.gson.GsonConverterFactory

class ClientAPIService {
    companion object {
        var hostURL = "https://dev5.pinpointdigital.com/"
        var baseURL = "https://dev5.pinpointdigital.com/api/"
        var client: ClientLoginAPIClient = ClientLoginAPIClient.create()

        fun requestLogin(
            email: String, password: String, callback: (succeeded: Boolean, result: UserData?) -> Unit
        ) {
            client.requestLogin(email,password).enqueue(object: Callback<UserData>{

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.d(
                        "ClientAPIService",
                        "Could not get network status ${t.localizedMessage}"
                    )
                    callback(false, null)
                }
                override fun onResponse(call: Call<UserData>, response: Re2ponse<UserData>) {
                    Log.d("ClientAPIService", "Got the user back ${response.body()}")
                    val code = response.code()
                    if (response.body() != null) {
                        var postResponse = response.body()!!
                        callback(true, postResponse)
                    } else {
                        callback(false, null)
                    }
                }
            })
        }

        fun requestRegister(
            email: String, password: String, password_confirmation: String, first_name: String, last_name: String, callback: (succeeded: Boolean, result: UserData?) -> Unit
        ) {
            client.requestRegister(email,password,password_confirmation,first_name,last_name).enqueue(object: Callback<UserData>{

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    Log.d(
                        "ClientAPIService",
                        "Could not get network status ${t.localizedMessage}"
                    )
                    callback(false, null)
                }
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    Log.d("ClientAPIService", "Got the user back ${response.body()}")
                    if (response.body() != null) {
                        var postResponse = response.body()!!
                        callback(true, postResponse)
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

    companion object Factory {
        fun create(): ClientLoginAPIClient {
            Log.d("Client Portal Service", "Create Factory")
            var retrofit = Retrofit.Builder()
                .baseUrl(ClientAPIService.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ClientLoginAPIClient::class.java)
        }
    }
}

