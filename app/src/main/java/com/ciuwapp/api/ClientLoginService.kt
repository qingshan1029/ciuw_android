package com.ciuwapp.api
import android.util.Log

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import com.ciuwapp.data.UserData
import retrofit2.converter.gson.GsonConverterFactory

class ClientLoginService {
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
                        "ClientLoginService",
                        "Could not get network status ${t.localizedMessage}"
                    )
                    callback(false, null)
                }
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    Log.d("ClientPortalService", "Got the user back ${response.body()}")

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
    @FormUrlEncoded
    @POST("login")

    fun requestLogin(
        @Field("email") email:String,
        @Field("password") password:String
    ) : Call<UserData>

    companion object Factory {
        fun create(): ClientLoginAPIClient {
            Log.d("Client Portal Service", "Create Factory")
            var retrofit = Retrofit.Builder()
                .baseUrl(ClientLoginService.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ClientLoginAPIClient::class.java)
        }
    }
}

