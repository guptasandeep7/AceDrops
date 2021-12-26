package com.example.acedrops.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.acedrops.model.Message
import com.example.acedrops.model.UserData
import com.example.acedrops.network.ServiceBuilder
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    var userDetails = MutableLiveData<UserData>()
    var errorMessage = MutableLiveData<String>()

    fun login(email: String, pass: String) {
        val request = ServiceBuilder.buildService()
        val call = request.login(UserData(email = email, password = pass))
        call.enqueue(object : Callback<UserData?> {
            override fun onResponse(call: Call<UserData?>, response: Response<UserData?>) {
                if (response.isSuccessful) {
                    Log.d("RESPONSE BODY", response.body().toString())
                    userDetails.value = response.body()
                }
                else if(response.code()==401) {
                    errorMessage.value = "Wrong password"
                }
                else{
                    errorMessage.value = "User does not exists please signup"
                }
            }

            override fun onFailure(call: Call<UserData?>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }
}